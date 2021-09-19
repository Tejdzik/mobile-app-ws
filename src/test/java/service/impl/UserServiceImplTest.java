package service.impl;

import com.tadziu.app.ws.exceptions.UserServiceException;
import com.tadziu.app.ws.io.entity.AddressEntity;
import com.tadziu.app.ws.io.entity.UserEntity;
import com.tadziu.app.ws.io.repositories.PasswordResetTokenRepository;
import com.tadziu.app.ws.io.repositories.UserRepository;
import com.tadziu.app.ws.service.impl.UserServiceImpl;
import com.tadziu.app.ws.shared.AmazonSES;
import com.tadziu.app.ws.shared.dto.AddressDTO;
import com.tadziu.app.ws.shared.dto.UserDTO;
import com.tadziu.app.ws.shared.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {


    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    AmazonSES amazonSES;

    @InjectMocks
    UserServiceImpl userService;


    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("tejdzik17@gmail.com");
        userEntity.setEmailVerificationToken("testToken");
        userEntity.setAddresses(getAddressesEntity());
    }

    String userId = "hhty57ehfy";
    String encryptedPassword = "74hghd8474jf";
    UserEntity userEntity;

    @Test
    final void testGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDTO userDTO = userService.getUser("test22@test.com");

        assertNotNull(userDTO);
        assertEquals("Sergey", userDTO.getFirstName());
//        fail("Not yet implemented");
    }

    @Test
    final void testGetUser_UsernameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userService.getUser("test22@test.com");
                }
        );
    }

    @Test
    final void testCreateUser_UserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Sergey");
        userDTO.setLastName("Kargopolov");
        userDTO.setPassword("1111");
        userDTO.setEmail("test22@test.com");

        assertThrows(UserServiceException.class,
                () -> {
                    userService.createUser(userDTO);
                }
        );
    }

    @Test
    final void testCreateUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("hdfhskjdf3884");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDTO.class));

        UserDTO userDTO = new UserDTO();
        userDTO.setAddresses(getAddressesDTO());
        userDTO.setFirstName("Sergey");
        userDTO.setLastName("Kargopolov");
        userDTO.setPassword("1111");
        userDTO.setEmail("test22@test.com");

        UserDTO storedUserDetails = userService.createUser(userDTO);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils,times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("1111");
        verify(userRepository,times(1)).save(any(UserEntity.class));
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
