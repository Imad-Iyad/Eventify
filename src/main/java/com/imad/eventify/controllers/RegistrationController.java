package com.imad.eventify.controllers;

import com.imad.eventify.model.DTOs.RegistrationDTO;
import com.imad.eventify.model.DTOs.RegistrationResDTO;
import com.imad.eventify.services.InvitationService;
import com.imad.eventify.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final InvitationService invitationService;
    private final RegistrationService registrationService;
    /**
     * Endpoint to register a user for an event.
     * This handles both public registrations and private ones via invitation.
     *
     * @param registrationDTO DTO containing userId, eventId, and optionally invitationId.
     * @return The created Registration details.
     */
    @PostMapping
    public ResponseEntity<RegistrationResDTO> registerForEvent(@RequestBody RegistrationDTO registrationDTO) {
        RegistrationResDTO createdRegistration = registrationService.registerToEvent(registrationDTO);
        return new ResponseEntity<>(createdRegistration, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get registration details using the unique registration token.
     * This can be used to verify a ticket/QR code.
     *
     * @param token The unique token generated during registration.
     * @return The registration details.
     */
    @GetMapping("/{token}")
    public ResponseEntity<RegistrationResDTO> getRegistrationByToken(@PathVariable String token) {
        RegistrationResDTO registrationResDTO = registrationService.getRegistrationByToken(token);
        return ResponseEntity.ok(registrationResDTO);
    }

    /**
     * Endpoint: GET /by-invitation/{token}

     * 🎯 Purpose:
     * ------------
     * This endpoint is used when an invitee clicks on the invitation link
     * (which contains a unique token).

     * ✅ Requirements:
     * ----------------
     * - The user must be authenticated (logged in).
     *   - If the user is not logged in, the frontend must redirect them to
     *     the login/sign-up page first.
     * - Once authenticated, this endpoint will provide the required
     *   registration details to proceed.

     * 🔄 Flow:
     * --------
     * 1. The invitee clicks the invitation link `/by-invitation/{token}` from their email.
     * 2. If the user is not logged in → frontend redirects them to the login/sign-up page.
     * 3. After successful authentication:
     *    - Backend fetches the Invitation using the provided token.
     *    - Retrieves the currently logged-in user from the session
     *      (via @AuthenticationPrincipal).
     *    - Builds and returns a RegistrationReqDTO with:
     *        - userId: the authenticated user’s ID.
     *        - eventId: the ID of the event related to the invitation.
     *        - invitationId: the ID of the invitation.
     *        - inviteeEmail: the email of the invitee (from the invitation).
     *        - attendanceConfirmed: false (default).

     * 📝 Notes for Frontend:
     * ----------------------
     * - After receiving this DTO, the frontend should display the registration form
     *   so the invitee can confirm attendance or fill in additional required details.
     * - The frontend should not manually provide userId → it must come from the session
     *   (ensuring security).
     */
    @GetMapping("/by-invitation/{token}")
    public ResponseEntity<RegistrationDTO> getRegistrationFromInvitation(@PathVariable String token) {
        return ResponseEntity.ok(invitationService.getInvitationByToken(token));
    }
}