package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.*;
import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.DTOs.RegistrationResDTO;
import com.imad.eventify.model.entities.Event;
import com.imad.eventify.model.entities.Invitation;
import com.imad.eventify.model.entities.Registration;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.entities.enums.InvitationStatus;
import com.imad.eventify.model.mappers.RegistrationMapper;
import com.imad.eventify.repositories.EventRepository;
import com.imad.eventify.repositories.InvitationRepository;
import com.imad.eventify.repositories.RegistrationRepository;
import com.imad.eventify.services.EmailService;
import com.imad.eventify.services.RegistrationService;
import com.imad.eventify.services.UserService;
import com.imad.eventify.utils.QRCodeGenerator;
import com.imad.eventify.utils.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;
    private final RegistrationMapper registrationMapper;
    private final EmailService emailService;
    private final UserService userService;

    /**
     * Handles the registration process for both PUBLIC and PRIVATE events.
     *
     * <p><b>Workflow:</b></p>
     *
     * <ol>
     *   <li><b>Get current user</b>:
     *       - Extract the currently authenticated user.
     *       - Ensure the user is active (not disabled/deactivated).
     *   </li>
     *
     *   <li><b>Find the event</b>:
     *       - Fetch the event using {@code dto.getEventId()}.
     *       - If not found → throws {@link EventNotFoundException}.
     *   </li>
     *
     *   <li><b>Check event type</b>:
     *       - If event is <b>PUBLIC</b>:
     *         <ul>
     *           <li>`dto.invitationId` <u>must be null</u> (invitation not required).</li>
     *           <li>Frontend only needs to send <b>eventId</b>.</li>
     *         </ul>
     *       - If event is <b>PRIVATE</b>:
     *         <ul>
     *           <li>`dto.invitationId` <u>must be provided</u> (mandatory).</li>
     *           <li>Invitation must exist and not be already used.</li>
     *           <li>Frontend must send <b>eventId + invitationId</b>.</li>
     *         </ul>
     *   </li>
     *
     *   <li><b>Check duplicate registration</b>:
     *       - Prevents the same user from registering twice for the same event.
     *   </li>
     *
     *   <li><b>Invitation validation (PRIVATE only)</b>:
     *       - Ensure the invitation exists.
     *       - Ensure it hasn’t been used before.
     *       - Mark it as USED once the registration is successful.
     *   </li>
     *
     *   <li><b>Generate registration credentials</b>:
     *       - Create a unique registration token.
     *       - Generate a QR code from the token (used later for event check-in).
     *   </li>
     *
     *   <li><b>Persist registration</b>:
     *       - Save the registration entity in DB with:
     *         user, event, invitation (if any), token, QR code, timestamp.
     *   </li>
     *
     *   <li><b>Send confirmation email</b>:
     *       - Email is sent to the user’s email address with the QR code attached.
     *   </li>
     * </ol>
     *
     * <p><b>Frontend responsibilities:</b></p>
     * <ul>
     *   <li>For PUBLIC events → Call API with only <b>eventId</b>.</li>
     *   <li>For PRIVATE events → Call API with <b>eventId + invitationId</b>.</li>
     *   <li>After successful registration → Show success message to user
     *       (e.g., "Registration confirmed. Check your email for QR code").</li>
     *   <li>Handle error responses:
     *       <ul>
     *         <li>409 → Already registered</li>
     *         <li>404 → Event/Invitation not found</li>
     *         <li>403 → Inactive/Forbidden</li>
     *         <li>500 → Something went wrong (retry later)</li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * @param dto the registration request containing eventId (and invitationId if private).
     * @return a DTO with registration details (including QR code in Base64 or byte[]).
     * @throws EventNotFoundException if the event does not exist.
     * @throws RuntimeException for invalid state (duplicate registration, invalid invitation, etc.).
     */

    @Override
    @Transactional
    public RegistrationResDTO registerToEvent(RegistrationDTO dto) {
        User user = userService.getCurrentUserEntity();
        UserValidator.assertUserIsActive(user);

        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + dto.getEventId()));

        // Check event type
        switch (event.getEventType()) {
            case PUBLIC:
                if (dto.getInvitationId() != null) {
                    throw new InvalidRegistrationException("Public events do not require an invitation");
                }
                break;

            case PRIVATE:
                if (dto.getInvitationId() == null) {
                    throw new InvalidRegistrationException("Invitation is required for private events");
                }
                break;
        }

        // Prevent duplicate registration
        boolean alreadyRegistered = registrationRepository.existsByUserAndEvent(user, event);
        if (alreadyRegistered) {
            throw new UserAlreadyRegisteredException("User is already registered for this event");
        }

        // Validate invitation (only for private)
        Invitation invitation = null;
        if (dto.getInvitationId() != null) {
            invitation = validateInvitation(dto.getInvitationId());
        }

        // Generate Token and QR
        String token = UUID.randomUUID().toString();
        byte[] qrCode = QRCodeGenerator.generateQRCode(token, 250, 250);

        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registrationToken(token)
                .qrCode(qrCode)
                .invitation(invitation)
                .registeredAt(LocalDateTime.now())
                .build();

        Registration saved = registrationRepository.save(registration);

        // Update invitation status if used
        if (invitation != null) {
            invitation.setStatus(InvitationStatus.USED);
            invitation.setUsedAt(LocalDateTime.now());
            invitationRepository.save(invitation);
        }

        // Send confirmation email with QR
        try {
            emailService.sendRegistrationConfirmation(user.getEmail(), event.getTitle(), qrCode);
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send registration confirmation email", e);
        }

        return registrationMapper.toDTO(saved);
    }

    private Invitation validateInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        if (invitation.getStatus() == InvitationStatus.USED) {
            throw new InvitationAlreadyUsedException("This invitation has already been used");
        }

        boolean invitationUsed = registrationRepository.existsByInvitation(invitation);
        if (invitationUsed) {
            throw new InvitationAlreadyUsedException("This invitation has already been used");
        }

        return invitation;
    }


    @Override
    @Transactional
    public RegistrationResDTO getRegistrationByToken(String token) {
        Registration registration = registrationRepository.findByRegistrationToken(token)
                .orElseThrow(() -> new RuntimeException("Registration not found with token: " + token));

        if (!registration.getUser().getId().equals(userService.getCurrentUserEntity().getId())) {
            throw new AccessDeniedException("You are not allowed to access this registration");
        }

        UserValidator.assertUserIsActive(registration.getUser());
        return registrationMapper.toDTO(registration);
    }

}
