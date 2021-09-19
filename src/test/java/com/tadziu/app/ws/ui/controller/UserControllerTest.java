package com.tadziu.app.ws.ui.controller;

import com.tadziu.app.ws.io.entity.AddressEntity;
import com.tadziu.app.ws.service.impl.UserServiceImpl;
import com.tadziu.app.ws.shared.dto.AddressDTO;
import com.tadziu.app.ws.shared.dto.UserDTO;
import com.tadziu.app.ws.ui.model.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;

    UserDTO userDTO;

    final String USER_ID = "bfjksdslkdjf";

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Sergey");
        userDTO.setLastName("Kargopolov");
        userDTO.setEmail("test22@test.com");
        userDTO.setEmailVerificationStatus(Boolean.FALSE);
        userDTO.setEmailVerificationToken(null);
        userDTO.setUserId(USER_ID);
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setEncryptedPassword("yzsruc123l");
    }

    @Test
    final void testGetUser() {
        when(userService.getUserByUserID(anyString())).thenReturn(null);
        UserRest userRest = userController.getUser(USER_ID);
        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDTO.getFirstName(), userRest.getFirstName());
        assertEquals(userDTO.getLastName(), userRest.getLastName());
        assertTrue(userDTO.getAddresses().size() == userRest.getAddresses().size());
    }

    private List<AddressDTO> getAddressesDTO() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setType("shipping");
        addressDTO.setCity("Vancouver");
        addressDTO.setCountry("Canada");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setStreetName("123 Street Name");

        AddressDTO billingAddressDTO = new AddressDTO();
        billingAddressDTO.setType("billing");
        billingAddressDTO.setCity("Vancouver");
        billingAddressDTO.setCountry("Canada");
        billingAddressDTO.setPostalCode("ABC123");
        billingAddressDTO.setStreetName("123 Street Name");


        List<AddressDTO> addresses = new ArrayList<>();
        addresses.add(addressDTO);
        addresses.add(billingAddressDTO);

        return addresses;
    }

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDTO> addresses = getAddressesDTO();

        Type listType = new TypeToken<List<AddressEntity>>() {
        }.getType();

        return new ModelMapper().map(addresses, listType);
    }
}
