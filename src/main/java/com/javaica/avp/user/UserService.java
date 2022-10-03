package com.javaica.avp.user;

import com.javaica.avp.exception.AlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    public AppUser createUser(AppUser user) {
        AppUser prepared = user.withId(null)
                .withPassword(encoder.encode(user.getPassword()))
                .withRole(UserRole.USER)
                .withTeamId(null);
        UserEntity entity = modelToEntity(prepared);
        if (userRepository.existsByUsername(entity.getUsername()))
            throw new AlreadyExistsException();
        entity = userRepository.save(entity);
        log.debug("User {} registered", entity.getUsername());
        return entityToModel(entity);
    }

    public Optional<AppUser> findUser(long id) {
        return userRepository.findById(id).map(this::entityToModel);
    }

    public Optional<AppUser> findUser(String username) {
        return userRepository.findByUsername(username).map(this::entityToModel);
    }

    public List<AppUser> getAllUsers() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::entityToModel)
                .collect(Collectors.toList());
    }

    private UserEntity modelToEntity(AppUser user) {
        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .teamId(user.getTeamId())
                .build();
    }

    private AppUser entityToModel(UserEntity entity) {
        return AppUser.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .role(entity.getRole())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .teamId(entity.getTeamId())
                .build();
    }
}
