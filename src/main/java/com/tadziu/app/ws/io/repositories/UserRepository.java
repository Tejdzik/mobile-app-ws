package com.tadziu.app.ws.io.repositories;

import com.tadziu.app.ws.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
    //CrudRepository<UserEntity, Long>
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String id);
    UserEntity findUserByEmailVerificationToken(String token);
}
