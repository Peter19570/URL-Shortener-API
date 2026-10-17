package com.peter.shortener.features.auth.config.userservice;

import com.peter.shortener.features.shared.dto.UserPrincipal;
import com.peter.shortener.features.user.model.User;
import com.peter.shortener.features.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found"));
        return UserPrincipal.fromDatabase(user);
    }

}
