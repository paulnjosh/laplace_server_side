package com.example.Primarch.UserProfile;

import com.example.Primarch.User.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepo extends JpaRepository<UserProfile,Long> {

    Optional<UserProfile> findByEmail(String email);
}
