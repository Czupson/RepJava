package com.example;

import org.example.reservation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.InOrder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservationService")
class ReservationServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationEmailService emailService;

    @Mock
    private ConfirmationCodeGenerator codeGenerator;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private ReservationService reservationService;

    @Captor
    private ArgumentCaptor<Reservation> reservationCaptor;

    @Captor
    private ArgumentCaptor<ConfirmationEmail> emailCaptor;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    private Room room;
    private TimeSlot timeSlot;

    @BeforeEach
    void setUp() {
        room = new Room(
                "ROOM-A",
                "Sala Konferencyjna A",
                20,
                true
        );

        timeSlot = new TimeSlot(
                LocalDate.of(2026, 6, 15),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );
    }

    private void setupSuccessfulReservation() {

        when(roomRepository.findById("ROOM-A"))
                .thenReturn(Optional.of(room));

        when(reservationRepository.existsByRoomIdAndTimeSlot(
                anyString(),
                any(TimeSlot.class)))
                .thenReturn(false);

        when(codeGenerator.generate())
                .thenReturn("RES-TEST1234");

        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(inv -> {
                    Reservation reservation = inv.getArgument(0);
                    reservation.setId(1L);
                    return reservation;
                });
    }


    @Nested
    class ArgumentCaptorTests {

        @Test
        void shouldSaveReservationWithCorrectData() {

            setupSuccessfulReservation();

            reservationService.createReservation(
                    "ROOM-A",
                    "anna@firma.pl",
                    timeSlot,
                    10
            );

            verify(reservationRepository)
                    .save(reservationCaptor.capture());

            Reservation saved = reservationCaptor.getValue();

            assertThat(saved.getRoomId()).isEqualTo("ROOM-A");
            assertThat(saved.getOrganizerEmail())
                    .isEqualTo("anna@firma.pl");
            assertThat(saved.getAttendees()).isEqualTo(10);
            assertThat(saved.getConfirmationCode())
                    .isEqualTo("RES-TEST1234");
            assertThat(saved.getStatus())
                    .isEqualTo(ReservationStatus.CONFIRMED);
        }
        @Test
        void shouldSendEmailWithCorrectContent() {

            setupSuccessfulReservation();

            reservationService.createReservation(
                    "ROOM-A",
                    "anna@firma.pl",
                    timeSlot,
                    10
            );

            verify(emailService)
                    .sendConfirmation(emailCaptor.capture());

            ConfirmationEmail email = emailCaptor.getValue();

            assertThat(email.getRecipientEmail())
                    .isEqualTo("anna@firma.pl");

            assertThat(email.getSubject())
                    .contains("Sala Konferencyjna A");

            assertThat(email.getBody())
                    .contains("RES-TEST1234")
                    .contains("2026-06-15");
        }

        @Test
        void shouldPublishEventWithReservationDetails() {

            setupSuccessfulReservation();

            reservationService.createReservation(
                    "ROOM-A",
                    "anna@firma.pl",
                    timeSlot,
                    10
            );

            ArgumentCaptor<String> eventTypeCaptor =
                    ArgumentCaptor.forClass(String.class);

            ArgumentCaptor<String> detailsCaptor =
                    ArgumentCaptor.forClass(String.class);

            verify(eventPublisher)
                    .publish(
                            eventTypeCaptor.capture(),
                            detailsCaptor.capture()
                    );

            assertThat(eventTypeCaptor.getValue())
                    .isEqualTo("RESERVATION_CREATED");

            assertThat(detailsCaptor.getValue())
                    .contains("RES-TEST1234")
                    .contains("Sala Konferencyjna A");
        }
    }

    @Nested
    class InOrderTests {

        @Test
        void shouldExecuteOperationsInCorrectOrder() {

            setupSuccessfulReservation();

            reservationService.createReservation(
                    "ROOM-A",
                    "anna@firma.pl",
                    timeSlot,
                    10
            );

            InOrder inOrder = inOrder(
                    roomRepository,
                    reservationRepository,
                    codeGenerator,
                    emailService,
                    eventPublisher
            );

            inOrder.verify(roomRepository)
                    .findById("ROOM-A");

            inOrder.verify(reservationRepository)
                    .existsByRoomIdAndTimeSlot(
                            eq("ROOM-A"),
                            any(TimeSlot.class)
                    );

            inOrder.verify(codeGenerator)
                    .generate();

            inOrder.verify(reservationRepository)
                    .save(any(Reservation.class));

            inOrder.verify(emailService)
                    .sendConfirmation(any(ConfirmationEmail.class));

            inOrder.verify(eventPublisher)
                    .publish(anyString(), anyString());
        }
    }
    @Nested
    class ThenAnswerTests {

        @Test
        void shouldGenerateUniqueCodesForMultipleReservations() {

            AtomicInteger codeCounter = new AtomicInteger(1);
            AtomicLong idCounter = new AtomicLong(1);

            when(roomRepository.findById(anyString()))
                    .thenReturn(Optional.of(room));

            when(reservationRepository.existsByRoomIdAndTimeSlot(
                    anyString(),
                    any(TimeSlot.class)))
                    .thenReturn(false);

            when(codeGenerator.generate())
                    .thenAnswer(inv ->
                            "RES-" + codeCounter.getAndIncrement());

            when(reservationRepository.save(any(Reservation.class)))
                    .thenAnswer(inv -> {
                        Reservation reservation = inv.getArgument(0);
                        reservation.setId(idCounter.getAndIncrement());
                        return reservation;
                    });

            TimeSlot secondSlot = new TimeSlot(
                    LocalDate.of(2026, 6, 16),
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 0)
            );

            reservationService.createReservation(
                    "ROOM-A",
                    "a@firma.pl",
                    timeSlot,
                    5
            );

            reservationService.createReservation(
                    "ROOM-A",
                    "b@firma.pl",
                    secondSlot,
                    5
            );

            verify(reservationRepository, times(2))
                    .save(reservationCaptor.capture());

            List<Reservation> reservations =
                    reservationCaptor.getAllValues();

            assertThat(reservations.get(0).getConfirmationCode())
                    .isEqualTo("RES-1");

            assertThat(reservations.get(1).getConfirmationCode())
                    .isEqualTo("RES-2");

            assertThat(reservations.get(0).getId())
                    .isEqualTo(1L);

            assertThat(reservations.get(1).getId())
                    .isEqualTo(2L);
        }

        @Test
        void shouldThrowWhenRoomNotFound() {

            when(roomRepository.findById("ROOM-X"))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                    reservationService.createReservation(
                            "ROOM-X",
                            "anna@firma.pl",
                            timeSlot,
                            10
                    ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("nie istnieje");

            verifyNoInteractions(
                    reservationRepository,
                    emailService,
                    codeGenerator,
                    eventPublisher
            );
        }

        @Test
        void shouldThrowWhenTooManyAttendees() {

            when(roomRepository.findById("ROOM-A"))
                    .thenReturn(Optional.of(room));

            assertThatThrownBy(() ->
                    reservationService.createReservation(
                            "ROOM-A",
                            "anna@firma.pl",
                            timeSlot,
                            25
                    ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("pojemność");

            verifyNoInteractions(
                    reservationRepository,
                    emailService,
                    codeGenerator,
                    eventPublisher
            );
        }


        @Test
        void shouldThrowWhenTimeSlotAlreadyBooked() {

            when(roomRepository.findById("ROOM-A"))
                    .thenReturn(Optional.of(room));

            when(reservationRepository.existsByRoomIdAndTimeSlot(
                    anyString(),
                    any(TimeSlot.class)))
                    .thenReturn(true);

            assertThatThrownBy(() ->
                    reservationService.createReservation(
                            "ROOM-A",
                            "anna@firma.pl",
                            timeSlot,
                            10
                    ))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("zarezerwowana");

            verify(codeGenerator, never()).generate();

            verify(reservationRepository, never())
                    .save(any(Reservation.class));
        }
    }
}