# Lombok Refactoring Changes Summary

This file documents the changes made to replace the boilerplate code (getters, setters, and constructors) in the entity classes of the `BusTicketBookingSystem` with Lombok annotations.

---

## 1. Maven Dependency Added

In the [pom.xml](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/pom.xml), Lombok was integrated into the project's dependencies:

```xml
        <!-- ✅ Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
```

---

## 2. Refactored JPA Entities

All entity classes under the package `com.ash.bbus.web.BusTicketBookingSystem.entity` were updated. In each, boilerplate code (getters, setters, default constructors, and sometimes all-args constructors) was removed and replaced with Lombok annotations.

| Entity Class File | Applied Lombok Annotations | Custom Code Preserved / Read-Only Fields |
| :--- | :--- | :--- |
| [Booking.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Booking.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | Preserved custom constructor `Booking(Long, User, Trip, BookingStatus, LocalDateTime, BigDecimal)` |
| [Bus.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Bus.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | Added `@ManyToOne` relationship to `BusOperator`. |
| [BusOperator.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/BusOperator.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` | New entity. Has `@OneToMany` to `Bus`. |
| [Cancellation.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Cancellation.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved `@PrePersist onCreate()` hook.<br>- Preserved custom refund calculation constructor.<br>- Restricted `cancelledAt` timestamp with `@Setter(AccessLevel.NONE)`. |
| [ChatMessage.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/ChatMessage.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` | New entity. Has `@ManyToOne` relationship to `User`. |
| [Coupon.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Coupon.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` | New entity. |
| [BlacklistedToken.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/BlacklistedToken.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder` | New entity. (Replaces empty `BlackListToken.java`). |
| [Passenger.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Passenger.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | Fully replaced standard constructors, getters, and setters. |
| [Payment.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Payment.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | - Preserved `@PrePersist onCreate()` & `@PreUpdate onUpdate()` lifecycle hooks.<br>- Restricted `createdAt` and `updatedAt` timestamps with `@Setter(AccessLevel.NONE)`. |
| [Route.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Route.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | Fully replaced standard constructors, getters, and setters. |
| [Seat.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Seat.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | Fully replaced standard constructors, getters, and setters. |
| [Ticket.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Ticket.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved `@PrePersist onCreate()` lifecycle hook.<br>- Preserved custom constructor `Ticket(String, Booking)`.<br>- Preserved business helper method `invalidate()`.<br>- Restricted `issuedAt` timestamp with `@Setter(AccessLevel.NONE)`. |
| [Trip.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Trip.java) | `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` | Fully replaced standard constructors, getters, and setters. |
| [TripSeat.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/TripSeat.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved optimistic locking `@Version private int version`.<br>- Preserved custom constructor `TripSeat(Long, Trip, Seat, SeatStatus)`.<br>- Preserved business helper methods: `lockSeat()`, `releaseLock()`, `confirmBooking()`, `isLockExpired()`. |
| [User.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/User.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved `@PrePersist onCreate()` lifecycle hook.<br>- Preserved Spring Security `UserDetails` implementation methods: `getAuthorities()`, `getUsername()`, `getPassword()`, `isAccountNonExpired()`, `isAccountNonLocked()`, `isCredentialsNonExpired()`, `isEnabled()`. |
| [Wallet.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/Wallet.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved optimistic locking `@Version private int version`.<br>- Preserved `@PrePersist onCreate()` lifecycle hook.<br>- Preserved custom constructor `Wallet(User)`.<br>- Preserved business helper methods: `credit()`, `debit()`, `hasSufficientBalance()`.<br>- Restricted `createdAt` timestamp with `@Setter(AccessLevel.NONE)`. |
| [WalletTransaction.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/entity/WalletTransaction.java) | `@Getter`, `@Setter`, `@NoArgsConstructor` | - Preserved `@PrePersist onCreate()` lifecycle hook.<br>- Preserved custom constructor `WalletTransaction(BigDecimal, BigDecimal, TransactionType, String, Long, Wallet)`.<br>- Restricted `createdAt` timestamp with `@Setter(AccessLevel.NONE)`. |

---

## 3. Added DTO Enums

Three new enum classes were created under the package `com.ash.bbus.web.BusTicketBookingSystem.dto.enums`:
- [ChatIntent.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/dto/enums/ChatIntent.java): Defines intents like `SEARCH_TRIP`, `SEARCH_ROUTE`, `CANCEL_BOOKING`, etc.
- [ChatRole.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/dto/enums/ChatRole.java): Defines roles: `USER`, `ASSISTANT`, `SYSTEM`.
- [RecommendationType.java](file:///c:/Users/itspa/FBS/Bus%20Ticket%20System/BusTicketBookingSystem/src/main/java/com/ash/bbus/web/BusTicketBookingSystem/dto/enums/RecommendationType.java): Defines recommendation types: `POPULAR`, `FREQUENT_ROUTE`, `BEST_MATCH`, etc.

---

## 4. Services, Implementations and Controllers Implemented

The following new interfaces, controllers, and their implementations were developed:
1. `ChatbotService`: Integrated `ChatRequestDTO`, `ChatResponseDTO`, and basic intent-based message processing.
2. `BusOperatorService`: Complete CRUD and search for `BusOperator` mapped to `Bus`.
3. `CouponService`: Added methods for creating, updating, deleting, fetching all coupons, and applying coupons during checkout (with discount rules logic).
4. `TokenBlacklistService`: Token invalidation storage and check.
5. `DynamicPricingService`: Dummy algorithm to calculate dynamic pricing based on trip departure and time.
6. `RecommendationService`: Trip recommendations based on user history/destination.
7. `SeatPreferenceService`: Save and fetch users' preferred seat types.
8. `QRCodeService`: Generation of QR codes based on `Booking` ID.
9. `InvoiceService`: PDF/Text invoice generation for completed bookings.

All implementations make use of `@RequiredArgsConstructor` (Lombok) for dependency injection.

---

## 5. Pricing Refactoring (Double to BigDecimal)
All logic referencing ticket/trip prices using the `double` data type has been strictly refactored to use `java.math.BigDecimal` for absolute precision. The following files were updated:
- `DynamicPricingService` and `DynamicPricingServiceImpl`
- `DynamicPricingController`
- `TripDao` and `TripDaoImpl` (`findByPriceRange` method)

---

## 6. Verification Details
The refactored classes were verified using the Maven Wrapper compiler:
```powershell
.\mvnw.cmd clean compile
```
**Result**: `BUILD SUCCESS` (Compiled cleanly with Lombok processors active).

---

## 7. Advanced Chatbot Integration

We have successfully integrated the `ChatbotServiceImpl` with the following key changes:

### Spring AI Dependency Added
- **File modified:** `pom.xml`
- **Details:** Added `spring-ai-bom` (version `1.0.0-M1`) and `spring-ai-ollama-spring-boot-starter`. This enables the AI chat capabilities via Mistral/Ollama.

### Chatbot Service Implementation
- **File modified:** `src/main/java/com/ash/bbus/web/BusTicketBookingSystem/service/serviceImplementation/ChatbotServiceImpl.java`
- **Details:**
  - Added full Spring AI implementation to process intent recognition (e.g., `SEARCH_TRIP`, `CANCEL_BOOKING`).
  - Adapted the logic to work with your existing DTOs (`TripDTO`, `RouteDTO`, `BookingDTO`, `PaymentDTO`).
  - Adjusted field mapping for things like `journeyDate` (mapped to `travelDate`) and dynamically fetching missing fields via `TripService`.
  - Removed reliance on missing components, opting to cleanly use your existing `TripService` and `RouteService` for trip/route discovery.

### ChatMessage Repository Update
- **File modified:** `src/main/java/com/ash/bbus/web/BusTicketBookingSystem/repository/ChatMessageRepository.java`
- **Details:** Added the missing method `findBySessionIdOrderByCreatedAtAsc(String sessionId)` so the chatbot can correctly load conversational history for the current session.

---

## 8. Final Consistency Verification
- **Verified:** All newly added Service Implementation classes (e.g. `BusOperatorServiceImpl`, `CouponServiceImpl`, `ChatbotServiceImpl`) and Controllers.
- **Action Taken:** Refactored `ChatbotServiceImpl.java` to replace scattered `@Autowired` field injections with Lombok's `@RequiredArgsConstructor` and `private final` fields, perfectly matching the design patterns enforced in the rest of the refactored project.
- **Result:** Code compiles cleanly. All `Controller`s are mapped via `@RestController` and `@RequiredArgsConstructor`, and all `ServiceImpl`s properly fulfill their service interfaces and are managed by `@Service` and `@RequiredArgsConstructor`.

---

## 9. Entity Constructor Cleanup
- Removed custom constructors that were left behind in the following entities: `Booking.java`, `Cancellation.java`, `Ticket.java`, `TripSeat.java`, `Wallet.java`, and `WalletTransaction.java`.
- Applied Lombok's `@AllArgsConstructor` and `@Builder` to easily instantiate these entities via the Builder pattern in the service layer without cluttering the entity classes.
- Updated respective services (`BookingServiceImpl`, `TicketServiceImpl`, `WalletServiceImpl`) to build objects dynamically using `.builder()`.

## 10. Cancellation Policy Service Integration
- Implemented `CancellationPolicyService.java` and `CancellationPolicyServiceImpl.java` to handle dynamic, time-based refund calculations (e.g. 100% refund >= 24 hrs before departure, 70% >= 12 hrs, etc.).
- Integrated this service into `BookingServiceImpl.java`, completely removing the hard-coded 50% refund logic.

## 11. OpenRouter Spring AI Integration
- **Dependency Replaced:** Swapped out `spring-ai-ollama-spring-boot-starter` for `spring-ai-openai-spring-boot-starter` in `pom.xml` since OpenRouter provides a seamless OpenAI-compatible API interface.
- **Application Properties:** Added proper configuration keys in `application.properties`:
  - `spring.ai.openai.api-key` (Configured to use environment variable `OPENROUTER_API_KEY`)
  - `spring.ai.openai.base-url=https://openrouter.ai/api/v1`
  - `spring.ai.openai.chat.options.model` (Defaulted to `mistralai/mistral-7b-instruct:free` on OpenRouter)
- **ChatModel Integration:** Refactored `ChatbotServiceImpl.java` to use Spring AI's `ChatModel` instead of `ChatClient` to interface with the specified AI model.

## 12. Security Configuration Adjustments
- **Public Endpoints Updated:** Reviewed `SecurityConfig.java` and explicitly whitelisted the necessary endpoints that don't require login (`permitAll()`).
  - Added `/api/chat` so guests can access the BlueBus Concierge AI.
  - Added `GET /api/coupons/**` to the public GET endpoints so users can view active coupons without being logged in.
- **Strict Authentication:** Ensured that all other endpoints strictly require authentication (`.anyRequest().authenticated()`).

## 13. Bug Fixes
- **BusOperatorRepository:** Fixed startup crash (`No property 'operatorName' found for type 'BusOperator'`). Changed `findByOperatorNameContainingIgnoreCase` to `findByNameContainingIgnoreCase` in `BusOperatorRepository` and `BusOperatorServiceImpl` to correctly map to the `name` property of the `BusOperator` entity.

---

## 14. Service Review and Booking Flow Fixes

The following fixes were applied during the Bus Ticket Booking System service review:

### TicketServiceImpl
- Added null safety before reading `booking.getPayment().getMethod()` so ticket DTO generation no longer throws a `NullPointerException` for unpaid or pending bookings.
- Fixed ticket seat type mapping so `TripSeatDTO.seatType` is populated from the bus seat type linked through the physical seat.
- Added read-only transactions around ticket lookup/download flows to keep lazy booking, trip, bus, route, passenger, and seat data available while building the response.

### PaymentServiceImpl
- Replaced the hard-coded 50% refund calculation with `CancellationPolicyService.calculateRefundAmount(bookingId)`.
- Refunds now follow the configured policy:
  - `>= 24` hours before departure: 100%
  - `>= 12` hours before departure: 70%
  - `>= 4` hours before departure: 40%
  - `< 4` hours before departure: 0%

### BookingServiceImpl
- Updated the wallet credit description from a fixed `50% Refund` label to a generic refund label so the audit text matches the dynamic cancellation policy.

### TripServiceImpl and TripRepository
- Added `TripRepository.findAllWithBusAndRoute()` with `JOIN FETCH t.bus` and `JOIN FETCH t.route`.
- Updated `TripServiceImpl.getAllTrips()` to use the fetch-join query, reducing Hibernate N+1 queries when mapping trip responses.

### TripSeatServiceImpl
- Reviewed `releaseExpiredLocks()` and removed the incorrect `availableSeats` increment.
- Seat locking does not decrement `Trip.availableSeats`; only confirmed booking and cancellation adjust that count.

### BusServiceImpl
- Implemented `addSeatsToBus(...)`.
- Implemented `getSeatsByBus(...)`.
- Added seat persistence via `SeatRepository`, duplicate seat-number validation per bus, Bus-to-Seat linking, DTO mapping, and bus total seat count synchronization.

### QRCodeServiceImpl
- Replaced mock string bytes with real PNG QR code generation using ZXing.
- Added booking existence validation before generating the QR code.

### InvoiceServiceImpl
- Replaced plain text invoice bytes with a real PDF generated using PDFBox.
- Invoice PDF now includes booking ID, passenger details, route, seats, amount, payment method, and transaction ID.
- Added null-safe payment display for pending/unpaid bookings.

### UserServiceImpl
- Removed the hard-coded admin secret from Java code.
- Added `app.admin.secret=BUSADMIN2024` to `application.properties`.
- Injected the admin secret with `@Value("${app.admin.secret}")`.

### Dependencies Added
- Added ZXing dependencies for QR PNG generation:
  - `com.google.zxing:core:3.5.3`
  - `com.google.zxing:javase:3.5.3`
- Added PDFBox for real PDF invoice generation:
  - `org.apache.pdfbox:pdfbox:2.0.31`

### Verification
The project was compiled successfully with:

```powershell
.\mvnw.cmd compile
```

**Result:** `BUILD SUCCESS`

---

## 15. DTO Lombok Cleanup

- Removed hand-written getter and setter methods from DTO classes under `src/main/java/com/ash/bbus/web/BusTicketBookingSystem/dto`.
- Added Lombok `@Getter`, `@Setter`, and `@NoArgsConstructor` to DTOs that still had manual accessor boilerplate.
- Preserved useful custom constructors and helper/factory methods, including:
  - `ApiResponse.success(...)` / `ApiResponse.failure(...)`
  - `AuthResponseDTO(...)`
  - `UserResponseDTO(...)`
  - `ErrorResponse(...)`
  - `EmailDTO(...)` overloads and `hasAttachment()`
- Left existing Lombok-based chat DTOs unchanged because they were already using Lombok.

### Verification
The project was compiled successfully after the DTO cleanup:

```powershell
.\mvnw.cmd compile
```

**Result:** `BUILD SUCCESS`

---

## 16. Controller API Review and Smoke Testing

- Added missing bus seat controller APIs:
  - `POST /api/buses/{id}/seats`
  - `GET /api/buses/{id}/seats`
- Added explicit admin authorization on mutating coupon APIs:
  - `POST /api/coupons`
  - `PUT /api/coupons/{couponId}`
  - `DELETE /api/coupons/{couponId}`
- Added authenticated user/admin authorization on coupon apply:
  - `POST /api/coupons/apply`
- Added explicit admin authorization on mutating bus operator APIs:
  - `POST /api/operators`
  - `PUT /api/operators/{id}`
  - `DELETE /api/operators/{id}/deactivate`
- Updated `BookingController` cancellation email logic so it uses the dynamic cancellation policy refund amount instead of a hard-coded 50% calculation.

### API Smoke Test

The application was started on port `8081` because port `8080` was already in use. Login was tested with:

```json
{
  "email": "amit@gmail.com",
  "password": "password123"
}
```

**Login result:** `200 OK`, user `Amit Birajadar`, role `USER`.

The following endpoints were smoke-tested successfully:

- `GET /api/trips`
- `GET /api/routes`
- `GET /api/buses`
- `GET /api/coupons`
- `GET /api/wallet`
- `GET /api/wallet/transactions`
- `GET /api/bookings/my`
- `GET /api/users/1`
- `GET /api/operators`
- `GET /api/seat-preferences/1`
- `GET /api/tokens/is-blacklisted`
- `GET /api/trips/1`
- `GET /api/trips/1/seats`
- `GET /api/pricing/dynamic`
- `GET /api/trips/search`
- `GET /api/recommendations`
- `GET /api/buses/1`
- `GET /api/buses/1/seats`
- `GET /api/routes/1`
- `GET /api/bookings/1`
- `GET /api/payments/1`
- `GET /api/tickets/booking/1`
- `GET /api/invoices/1`
- `GET /api/qr-codes/1`

Also verified that a normal `USER` receives `403 Forbidden` for admin-only bus creation:

- `POST /api/buses`
