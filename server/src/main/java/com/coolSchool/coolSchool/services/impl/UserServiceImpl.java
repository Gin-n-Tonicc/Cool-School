package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.enums.Provider;
import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.user.UserCreateException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.AdminUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.auth.RegisterRequest;
import com.coolSchool.coolSchool.models.dto.request.CompleteOAuthRequest;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.VerificationToken;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.repositories.VerificationTokenRepository;
import com.coolSchool.coolSchool.security.CustomOAuth2User;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    private final VerificationTokenRepository verificationTokenRepository;

    /**
     * Creates a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return The created user.
     * @throws UserCreateException             If there is an issue creating the user.
     * @throws DataIntegrityViolationException If there is a data integrity violation while creating the user.
     * @throws ConstraintViolationException    If there is a constraint violation while creating the user.
     */
    @Override
    public User createUser(RegisterRequest request) {
        Role roleFromReq = request.getRole();

        if (roleFromReq == null || roleFromReq.equals(Role.ADMIN)) {
            request.setRole(Role.USER);
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserCreateException(messageSource, true);
        }

        try {
            User user = buildUser(request);
            user.setCreatedAt(LocalDateTime.now());
            return userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserCreateException(messageSource, true);
        } catch (ConstraintViolationException exception) {
            throw new UserCreateException(exception.getConstraintViolations());
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", messageSource));
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(x -> modelMapper.map(x, AdminUserDTO.class))
                .toList();
    }

    @Override
    public AdminUserDTO updateUser(Long id, AdminUserDTO userDTO, PublicUserDTO currentUser) {
        User userToUpdate = findById(id);

        if (userToUpdate.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(messageSource);
        }

        modelMapper.map(userDTO, userToUpdate);
        userToUpdate.setId(id);

        User updatedUser = userRepository.save(userToUpdate);
        return modelMapper.map(updatedUser, AdminUserDTO.class);
    }


    @Override
    public void deleteUserById(Long id, PublicUserDTO currentUser) {
        User user = findById(id);

        if (user.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException(messageSource);
        }

        user.setDeleted(true);
        userRepository.save(user);
    }

    /**
     * Processes the OAuth user obtained from the OAuth2 provider.
     * If the user does not exist in the database, a new user is created based on the OAuth user details.
     *
     * @param oAuth2User The OAuth2 user obtained from the OAuth provider.
     * @return The processed user.
     */
    @Override
    public User processOAuthUser(CustomOAuth2User oAuth2User) {
        User user = userRepository.findByEmail(oAuth2User.getEmail()).orElse(null);

        if (user == null) {
            // Default names after the user registers with OAUth2 and before they fill some other necessary information
            final String NAME_PLACEHOLDER = "CHANGE_NAME";
            final String DESCRIPTION_PLACEHOLDER = "CHANGE_THE_DESCRIPTION_PLEASE_CHANGE_THE_DESCRIPTION_PLEASEEE";
            final String ADDRESS_PLACEHOLDER = "CHANGE_ADDRESS";

            String username = oAuth2User.getName().toLowerCase()
                    .replaceAll("[^a-zA-Z0-9]", "");

            RegisterRequest registerRequest = new RegisterRequest();

            registerRequest.setEmail(oAuth2User.getEmail());
            registerRequest.setProvider(oAuth2User.getProvider());
            registerRequest.setUsername(username);
            registerRequest.setFirstname(NAME_PLACEHOLDER);
            registerRequest.setLastname(NAME_PLACEHOLDER);
            registerRequest.setRole(Role.USER);
            registerRequest.setDescription(DESCRIPTION_PLACEHOLDER);
            registerRequest.setAddress(ADDRESS_PLACEHOLDER);

            user = userRepository.save(buildUser(registerRequest));
        }

        return user;
    }

    @Override
    public User updateOAuth2UserWithFullData(CompleteOAuthRequest request, Long userId) {
        User user = findById(userId);
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setAddress(request.getAddress());
        user.setDescription(request.getDescription());
        user.setRole(request.getRole());
        user.setAdditionalInfoRequired(false);

        return userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", messageSource));
    }

    private User buildUser(RegisterRequest request) {
        boolean additionalInfoRequired = !request.getProvider().equals(Provider.LOCAL);

        User.UserBuilder userBuilder = User
                .builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(request.getRole())
                .provider(request.getProvider())
                .address(request.getAddress())
                .usernameField(request.getUsername())
                .description(request.getDescription())
                .additionalInfoRequired(additionalInfoRequired)
                .deleted(false);

        if (request.getPassword() != null) {
            userBuilder.password(passwordEncoder.encode(request.getPassword()));
        }

        return userBuilder.build();
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return verificationTokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }
}
