package com.tadziu.app.ws.service;

import com.tadziu.app.ws.shared.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO createUser(UserDTO user);

    UserDTO getUser(String email);

    UserDTO getUserByUserID(String userId);

    UserDTO updateUser(String userId, UserDTO user);

    void deleteUser(String userId);

    List<UserDTO> getUsers(int page, int limit);

    boolean verifyEmailToken(String token);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);
}
