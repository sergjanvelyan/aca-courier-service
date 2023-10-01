package com.aca.acacourierservice.repository;

import com.aca.acacourierservice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @AfterEach
    public void destroy(){
        userRepository.deleteAll();
    }
    @Test
    void testFindByEmail() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        Optional<User> userOptional = userRepository.findByEmail("storeAdmin@gmail.com");
        assertTrue(userOptional.isPresent());
        User savedStoreAdmin = userOptional.get();
        assertEquals(storeAdmin,savedStoreAdmin);

        Optional<User> adminOptional = userRepository.findByEmail("storeAdmin@gmail.com");
        assertTrue(adminOptional.isPresent());
    }
    @Test
    void testFindByInvalidEmail() {
        Optional<User> userOptional = userRepository.findByEmail("invalidEmail@gmail.com");
        assertFalse(userOptional.isPresent());
    }

    @Test
    void testFindAllByRole() {
        User storeAdmin = new User();
        storeAdmin.setEmail("storeAdmin@gmail.com");
        storeAdmin.setPassword("storeAdmin");
        storeAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        storeAdmin.setId(userRepository.save(storeAdmin).getId());

        User secondStoreAdmin = new User();
        secondStoreAdmin.setEmail("secondStoreAdmin@gmail.com");
        secondStoreAdmin.setPassword("secondStoreAdmin");
        secondStoreAdmin.setRole(User.Role.ROLE_STORE_ADMIN);
        secondStoreAdmin.setId(userRepository.save(secondStoreAdmin).getId());

        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin");
        admin.setRole(User.Role.ROLE_ADMIN);
        admin.setId(userRepository.save(admin).getId());

        Page<User> storeAdminsPage = userRepository.findAllByRole(User.Role.ROLE_STORE_ADMIN, PageRequest.of(0,5));
        assertEquals(2,storeAdminsPage.getNumberOfElements());
        List<User> storeAdmins = storeAdminsPage.getContent();

        User savedStoreAdmin = storeAdmins.get(0);
        assertEquals(storeAdmin,savedStoreAdmin);

        User savedSecondStoreAdmin = storeAdmins.get(1);
        assertEquals(secondStoreAdmin,savedSecondStoreAdmin);

        Page<User> adminsPage = userRepository.findAllByRole(User.Role.ROLE_ADMIN, PageRequest.of(0,5));
        assertEquals(1,adminsPage.getNumberOfElements());
        List<User> admins = adminsPage.getContent();

        User savedAdmin = admins.get(0);
        assertEquals(admin,savedAdmin);
    }

    @Test
    void testExistsByEmail() {
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("admin");
        admin.setRole(User.Role.ROLE_ADMIN);
        admin.setId(userRepository.save(admin).getId());

        assertTrue(userRepository.existsByEmail("admin@gmail.com"));
    }
    @Test
    void testExistsByInvalidEmail() {
        assertFalse(userRepository.existsByEmail("admin@gmail.com"));
    }
}