package com.javaica.avp.repository;

import com.javaica.avp.user.UserEntity;
import com.javaica.avp.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void findUserByUsernameTest() {
        Optional<UserEntity> userEntity = repository.findByUsername("user1");
        assertTrue(userEntity.isPresent());

        UserEntity user = userEntity.get();

        assertEquals(user.getName(), "User1");
        assertEquals(user.getSurname(), "Surname1");
        assertEquals(user.getEmail(), "user1@mail");
    }

    @Test
    public void findAllUsersByTeamIdTest() {
        List<UserEntity> userEntityList = repository.findAllByTeamId(1);

        assertEquals(userEntityList.size(), 3);
    }
}
