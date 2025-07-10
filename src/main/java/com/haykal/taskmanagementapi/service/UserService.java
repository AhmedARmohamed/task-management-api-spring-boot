package com.haykal.taskmanagementapi.service;

import com.haykal.taskmanagementapi.dto.ChangePasswordRequest;
import com.haykal.taskmanagementapi.dto.UpdateUserRequestDto;
import com.haykal.taskmanagementapi.dto.UserDto;
import com.haykal.taskmanagementapi.entity.User;
import com.haykal.taskmanagementapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.newPassword().equals(request.confirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        // save the new password
        repository.save(user);
    }

    public Optional<UserDto> getUserById(Integer id) {
        return repository.findById(id).map(this::convertToDto);
    }

    public List<UserDto> getAllUsers() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> searchUsersByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Integer id, UpdateUserRequestDto updateUserRequestDto, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Check if user is updating their own profile
        if (!user.getId().equals(id)) {
            throw new RuntimeException("You can only update your own profile");
        }

        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstname(updateUserRequestDto.firstName());
        existingUser.setLastname(updateUserRequestDto.lastName());
        existingUser.setDateOfBirth(updateUserRequestDto.dateOfBirth());

        User updatedUser = repository.save(existingUser);
        return convertToDto(updatedUser);
    }

    public void deleteUser(Integer id, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Check if user is deleting their own profile
        if (!user.getId().equals(id)) {
            throw new RuntimeException("You can only delete your own profile");
        }

        if (!repository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repository.deleteById(id);
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getDateOfBirth()
        );
    }

}
