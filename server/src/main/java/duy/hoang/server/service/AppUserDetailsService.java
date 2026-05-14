package duy.hoang.server.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import duy.hoang.server.entity.ProfileEntity;
import duy.hoang.server.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
     private final ProfileRepository profileRepository;

     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
          ProfileEntity existProfile = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));

          return User.builder().username(existProfile.getEmail()).password(existProfile.getPassword())
                    .authorities(Collections.emptyList()).build();
     }
}
