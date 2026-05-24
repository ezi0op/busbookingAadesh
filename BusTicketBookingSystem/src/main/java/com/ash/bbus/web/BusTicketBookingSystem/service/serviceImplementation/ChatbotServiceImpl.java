package com.ash.bbus.web.BusTicketBookingSystem.service.serviceImplementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatRequestDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.ChatResponseDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.PaymentDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.BookingDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.TripDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.RouteDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.EmailDTO;
import com.ash.bbus.web.BusTicketBookingSystem.dto.enums.ChatIntent;
import com.ash.bbus.web.BusTicketBookingSystem.dto.enums.ChatRole;
import com.ash.bbus.web.BusTicketBookingSystem.entity.ChatMessage;
import com.ash.bbus.web.BusTicketBookingSystem.entity.User;
import com.ash.bbus.web.BusTicketBookingSystem.repository.ChatMessageRepository;
import com.ash.bbus.web.BusTicketBookingSystem.repository.UserRepository;
import com.ash.bbus.web.BusTicketBookingSystem.service.BookingService;
import com.ash.bbus.web.BusTicketBookingSystem.service.ChatbotService;
import com.ash.bbus.web.BusTicketBookingSystem.service.EmailService;
import com.ash.bbus.web.BusTicketBookingSystem.service.PaymentService;
import com.ash.bbus.web.BusTicketBookingSystem.service.RouteService;
import com.ash.bbus.web.BusTicketBookingSystem.service.TripService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@lombok.RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {

	
	private final ChatModel chatModel;

	
	private final ChatMessageRepository chatMessageRepository;

	
	private final UserRepository userRepository;

	
	private final PaymentService paymentService;

	
	private final EmailService emailService;

	
	private final BookingService bookingService;

	
	private final TripService tripService;

	
	private final RouteService routeService;

	private static final String SYSTEM_PROMPT = """
			BLUE BUS BRAND IDENTITY:
			- Persona: You are the 'BlueBus Concierge', a premium, high-end AI travel assistant.
			- Tone: Professional, articulate, and proactive.

			PREMIUM FORMATTING RULES:
			1. Use **BOLD** for all important data: **PNRs**, **Prices**, **Dates**, **Statuses**, and **Key Actions**.
			2. For lists or descriptions, ALWAYS use a clear header followed by bullet points.
			3. Add a blank line between every point for maximum readability.
			4. Use Emojis to guide the user's eye (e.g., 🔍 for searching, 🎟️ for bookings, 💳 for payments).
			5. ALWAYS put your intent on a NEW line at the very end.
			   Example: "I can help with that! \n\n🎟️ **My Services:** \n• **Status:** Check PNRs \n• **Booking:** List history \n\nINTENT: LIST_MY_BOOKINGS"

			Supported intents:
			INTENT: SEARCH_TRIP, INTENT: SEARCH_ROUTE, INTENT: CANCEL_BOOKING, INTENT: GET_BOOKING_STATUS,
			INTENT: LIST_MY_BOOKINGS, INTENT: PAYMENT_HELP, INTENT: REFUND_STATUS, INTENT: UNKNOWN
			""";

	private String getSessionIdFromRequest(ChatRequestDTO request) {
		return request.getUserId() != null ? "SESSION_" + request.getUserId() : "SESSION_GUEST";
	}

	@Override
	public ChatResponseDTO chat(ChatRequestDTO request) {
		String sessionId = getSessionIdFromRequest(request);

		if (request == null || request.getMessage() == null || request.getMessage().trim().isEmpty()) {
			log.warn("Invalid chat request received");
			return ChatResponseDTO.builder().response("Invalid request. Message cannot be empty.")
					.intent(ChatIntent.UNKNOWN.name())
					.build();
		}

		try {
			List<ChatMessage> history = chatMessageRepository
					.findBySessionIdOrderByCreatedAtAsc(sessionId);

			String conversationContext = history.stream()
					.map(message -> message.getRole() + ": " + message.getContent()).collect(Collectors.joining("\n"));

			String fullPrompt = SYSTEM_PROMPT + "\n\nConversation History:\n" + conversationContext + "\n\nUser: "
					+ request.getMessage();

			String aiReply = chatModel.call(fullPrompt);

			if (aiReply == null || aiReply.trim().isEmpty()) {
				log.warn("Empty response received from AI for session: {}", sessionId);
				aiReply = "I'm having trouble processing your request. Please try again.";
			}

			ChatIntent intent = detectIntent(aiReply);

			if (intent == ChatIntent.UNKNOWN) {
				Long extractedId = extractBookingId(request.getMessage(), null);
				if (extractedId > 0) {
					intent = ChatIntent.GET_BOOKING_STATUS;
				}
			}

			String finalMessage = aiReply;

			if (intent == ChatIntent.SEARCH_TRIP) {
				try {
					List<TripDTO> trips = tripService.getAllTrips();
					String msg = request.getMessage().toUpperCase();
					
					List<TripDTO> matchedTrips = trips.stream().filter(t -> 
						msg.contains(t.getSource().toUpperCase()) || msg.contains(t.getDestination().toUpperCase())
					).collect(Collectors.toList());

					if (!matchedTrips.isEmpty()) {
						finalMessage = "I found these scheduled trips for your search: \n\n"
								+ formatTripsList(matchedTrips);
					} else {
						intent = ChatIntent.SEARCH_ROUTE;
					}
				} catch (Exception e) {
					log.error("Trip search failed", e);
					intent = ChatIntent.SEARCH_ROUTE;
				}
			}

			if (intent == ChatIntent.SEARCH_ROUTE) {
				try {
					String msg = request.getMessage().toUpperCase();
					List<RouteDTO> routes = routeService.getAllRoutes();

					List<RouteDTO> matchedRoutes = routes.stream().filter(r -> {
						String src = r.getSource().toUpperCase();
						String dest = r.getDestination().toUpperCase();
						return msg.contains(src) || msg.contains(dest);
					}).collect(Collectors.toList());

					if (!matchedRoutes.isEmpty()) {
						finalMessage = "I found these **Routes** we operate matching your request: \n\n"
								+ formatRoutesList(matchedRoutes);
					} else {
						finalMessage = "I couldn't find any buses or active routes matching your details. Would you like to check a different date or nearby cities?";
					}
				} catch (Exception e) {
					log.error("Route search failed", e);
					finalMessage = "I'm sorry, I couldn't find any information for that route right now.";
				}
			}

			if (intent == ChatIntent.CANCEL_BOOKING) {
				try {
					Long bookingId = extractBookingId(request.getMessage(), conversationContext);
					if (bookingId > 0) {
						paymentService.processRefund(bookingId);
						bookingService.cancelBooking(bookingId, request.getUserId());
						finalMessage = "I have successfully processed your request. **Booking #" + bookingId
								+ "** has been cancelled and your refund is on the way! You will receive an email confirmation shortly.";
					}
				} catch (Exception e) {
					log.debug("Extraction or cancellation failed: {}", e.getMessage());
				}
			}

			if (intent == ChatIntent.GET_BOOKING_STATUS) {
				try {
					Long bookingId = extractBookingId(request.getMessage(), conversationContext);
					if (bookingId > 0) {
						BookingDTO booking = bookingService.getBookingById(bookingId);
						if (booking != null) {
							TripDTO trip = tripService.getTripById(booking.getTripId());
							finalMessage = "Found it! Booking **#" + booking.getId() + "** is currently **" + booking.getStatus() + "**. \n\nJourney: "
									+ booking.getSource() + " → " + booking.getDestination() + " on "
									+ (trip != null ? trip.getTravelDate() : "Unknown date");
						}
					} else {
						finalMessage = "I couldn't find a booking with those details. Could you please double-check your Booking ID?";
					}
				} catch (Exception e) {
					log.debug("Booking status check failed: {}", e.getMessage());
				}
			}

			if (intent == ChatIntent.LIST_MY_BOOKINGS) {
				try {
					if (request.getUserId() != null) {
						List<BookingDTO> bookings = bookingService.getBookingsByUser(request.getUserId());
						if (bookings != null && !bookings.isEmpty()) {
							finalMessage = "You have **" + bookings.size()
									+ "** bookings in your account. Here are the most recent details:\n\n"
									+ formatBookingsList(bookings);
						} else {
							finalMessage = "Your booking history is currently empty. I can help you plan and book your first journey whenever you're ready!";
						}
					} else {
						finalMessage = "To provide you with personalized booking information, I'll need you to log in to your account first.";
					}
				} catch (Exception e) {
					log.debug("List bookings failed: {}", e.getMessage());
				}
			}

			if (intent == ChatIntent.PAYMENT_HELP) {
				try {
					Long bookingId = extractBookingId(request.getMessage(), conversationContext);
					if (bookingId > 0) {
						BookingDTO booking = bookingService.getBookingById(bookingId);
						if (booking != null) {
							if (request.getUserId() != null && !booking.getUserId().equals(request.getUserId())) {
								finalMessage = "For security reasons, I can only provide payment details for bookings made from your account.";
							} else {
								PaymentDTO paymentStatus = paymentService.getPaymentByBookingId(booking.getId());
								if (paymentStatus != null) {
									finalMessage = "I've retrieved the payment details for **Booking #" + booking.getId() + "**. \n\n"
											+ "• **Status:** " + paymentStatus.getStatus() + "\n"
											+ "• **Amount:** ₹" + paymentStatus.getAmount() + "\n"
											+ "• **Paid At:** " + (paymentStatus.getCreatedAt() != null ? paymentStatus.getCreatedAt() : "Pending");
								} else {
									finalMessage = "I found your booking #" + booking.getId() + ", but there is no payment record associated with it yet.";
								}
							}
						}
					} else {
						finalMessage = "I couldn't find a booking with that ID. Please provide a valid reference so I can check the payment status for you.";
					}
				} catch (Exception e) {
					log.error("Serious error in Payment Help", e);
					finalMessage = "I encountered an error while checking your payment. Please try again or contact our support team.";
				}
			}

			if (intent == ChatIntent.REFUND_STATUS) {
				try {
					Long bookingId = extractBookingId(request.getMessage(), conversationContext);
					if (bookingId > 0) {
						BookingDTO booking = bookingService.getBookingById(bookingId);
						if (booking != null) {
							if (request.getUserId() != null && !booking.getUserId().equals(request.getUserId())) {
								finalMessage = "I'm sorry, I can only provide refund status for bookings linked to your account.";
							} else {
								PaymentDTO refundStatus = paymentService.getPaymentByBookingId(booking.getId());
								if (refundStatus != null) {
									finalMessage = "Regarding your refund for **Booking #" + booking.getId() + "**: \n\n"
											+ "• **Status:** " + refundStatus.getStatus() + "\n"
											+ "• **Refund Amount:** ₹" + refundStatus.getRefundAmount() + "\n"
											+ "• **Estimated Date:** " + ("REFUNDED".equalsIgnoreCase(refundStatus.getStatus().name()) ? "Processed" : "5-7 business days from cancellation");
								} else {
									finalMessage = "I found your booking #" + booking.getId() + ", but there is no refund record for it yet.";
								}
							}
						}
					} else {
						finalMessage = "I couldn't find a booking with that ID. Please share your reference so I can check the refund status for you.";
					}
				} catch (Exception e) {
					log.error("Serious error in Refund Status", e);
					finalMessage = "I encountered an error while checking your refund status. Please try again later or contact our support team.";
				}
			}

			finalMessage = finalMessage.replaceAll("(?i)INTENT: [A-Z_]+", "").trim();

			saveMessage(request.getUserId(), sessionId, ChatRole.USER, request.getMessage());
			saveMessage(request.getUserId(), sessionId, ChatRole.ASSISTANT, finalMessage);

			return ChatResponseDTO.builder().response(finalMessage).intent(intent.name()).build();

		} catch (Exception e) {
			log.error("Unexpected error in chatbot service for session: {}", sessionId, e);
			return ChatResponseDTO.builder().response("An unexpected error occurred. Please try again later.")
					.intent(ChatIntent.UNKNOWN.name())
					.build();
		}
	}

	private void saveMessage(Long userId, String sessionId, ChatRole role, String content) {
		User user = null;
		if (userId != null) {
			user = userRepository.findById(userId).orElse(null);
		}
		chatMessageRepository.save(ChatMessage.builder().user(user).sessionId(sessionId).role(role).content(content).build());
	}

	private ChatIntent detectIntent(String reply) {
		String upper = reply.toUpperCase();
		if (upper.contains("INTENT: SEARCH_TRIP")) return ChatIntent.SEARCH_TRIP;
		if (upper.contains("INTENT: SEARCH_ROUTE")) return ChatIntent.SEARCH_ROUTE;
		if (upper.contains("INTENT: CANCEL_BOOKING")) return ChatIntent.CANCEL_BOOKING;
		if (upper.contains("INTENT: LIST_MY_BOOKINGS")) return ChatIntent.LIST_MY_BOOKINGS;
		if (upper.contains("INTENT: GET_BOOKING_STATUS")) return ChatIntent.GET_BOOKING_STATUS;
		if (upper.contains("INTENT: PAYMENT_HELP")) return ChatIntent.PAYMENT_HELP;
		if (upper.contains("INTENT: REFUND_STATUS")) return ChatIntent.REFUND_STATUS;
		return ChatIntent.UNKNOWN;
	}

	private Long extractBookingId(String message, String history) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\b(\\d{1,9})\\b");
		java.util.regex.Matcher matcher = pattern.matcher(message);
		if (matcher.find()) return Long.parseLong(matcher.group(1));

		if (history != null) {
			matcher = pattern.matcher(history);
			String lastMatch = "";
			while (matcher.find()) {
				lastMatch = matcher.group(1);
			}
			if (!lastMatch.isEmpty()) return Long.parseLong(lastMatch);
		}
		return 0L;
	}

	private String formatBookingsList(List<BookingDTO> bookings) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (BookingDTO b : bookings) {
			if (count++ >= 3) break;
			sb.append("• **Booking ID:** ").append(b.getId()).append(" | ")
					.append(b.getSource()).append(" → ")
					.append(b.getDestination()).append(" | ").append(b.getStatus()).append("\n");
		}
		return sb.toString();
	}

	private String formatTripsList(List<TripDTO> trips) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (TripDTO t : trips) {
			if (count++ >= 5) break;
			sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n").append("🚌 **Trip #").append(t.getId()).append("**\n")
					.append("• **Bus:** ").append(t.getBusName()).append(" (").append(t.getBusNumber()).append(")\n")
					.append("• **Journey:** ").append(t.getTravelDate()).append(" at **").append(t.getDepartureTime()).append("**\n")
					.append("• **Availability:** ").append(t.getAvailableSeats()).append(" seats left\n")
					.append("• **Fare:** ₹").append(t.getPrice()).append("\n\n");
		}
		sb.append("━━━━━━━━━━━━━━━━━━━━━━━━");
		return sb.toString();
	}

	private String formatRoutesList(List<RouteDTO> routes) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for (RouteDTO r : routes) {
			if (count++ >= 5) break;
			sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n").append("🛣️ **Route: ").append(r.getSource()).append(" → ")
					.append(r.getDestination()).append("**\n").append("• **Distance:** ").append(r.getDistance())
					.append(" km\n").append("• **Est. Duration:** ").append(r.getEstimatedDurationMinutes()).append(" mins\n\n");
		}
		sb.append("━━━━━━━━━━━━━━━━━━━━━━━━");
		return sb.toString();
	}
}
