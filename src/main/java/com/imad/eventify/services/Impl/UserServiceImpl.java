package com.imad.eventify.services.Impl;

import com.imad.eventify.Exceptions.EmailAlreadyExistsException;
import com.imad.eventify.model.DTOs.RegisterRequest;
import com.imad.eventify.model.DTOs.UserDTO;
import com.imad.eventify.model.DTOs.UserResponseDTO;
import com.imad.eventify.model.entities.User;
import com.imad.eventify.model.mappers.UserMapper;
import com.imad.eventify.repositories.UserRepository;
import com.imad.eventify.services.UserService;
import com.imad.eventify.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public UserResponseDTO getCurrentUserDTO() {
        return userMapper.toResponseDTO(getCurrentUserEntity());
    }

    @Override
    public User getCurrentUserEntity() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Partial Update
    @Override
    public UserResponseDTO updateUser(UserDTO userDTO) {
        User existing = getCurrentUserEntity();
        UserValidator.assertUserIsActive(existing);

        // If the email changes, verify the uniqueness
        if (userDTO.getEmail() != null && !existing.getEmail().equals(userDTO.getEmail()) &&
                userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email " + userDTO.getEmail() + " is already in use");
        }

        //Partial Update Using MapStruct
        userMapper.updateUserFromDto(userDTO, existing);
        existing.setUpdatedAt(LocalDateTime.now());

        return userMapper.toResponseDTO(userRepository.save(existing));
    }


    @Override
    public String deactivateCurrentUser() {
        User currentUser = getCurrentUserEntity();
        UserValidator.assertUserIsActive(currentUser);
        currentUser.setActive(false);
        userRepository.save(currentUser);
        return "Your Account With Email: " + currentUser.getEmail() + " has been deactivated";
    }

}
