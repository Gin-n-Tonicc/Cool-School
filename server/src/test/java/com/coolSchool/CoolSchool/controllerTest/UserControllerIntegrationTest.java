package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.UserController;
import com.coolSchool.coolSchool.models.dto.auth.AdminUserDTO;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerIntegrationTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetAllUsers() {
        List<AdminUserDTO> users = new ArrayList<>();
        users.add(new AdminUserDTO());
        users.add(new AdminUserDTO());

        users.get(0).setId(1L);
        users.get(0).setFirstname("User 1");
        users.get(1).setId(2L);
        users.get(1).setFirstname("User 2");

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<AdminUserDTO>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(users, responseEntity.getBody());
    }

    @Test
    void testUpdateUser() {
        AdminUserDTO userDTO = new AdminUserDTO();

        userDTO.setId(1L);
        userDTO.setFirstname("Updated User");

        when(userService.updateUser(1L, userDTO, null)).thenReturn(userDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<AdminUserDTO> responseEntity = userController.updateUser(1L, userDTO, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDTO, responseEntity.getBody());
    }

    @Test
    void testDeleteUser() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseEntity<Void> responseEntity = userController.deleteUser(1L, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
