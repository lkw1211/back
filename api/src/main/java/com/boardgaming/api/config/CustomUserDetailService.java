package com.boardgaming.api.config;

import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.springframework.security.core.userdetails.User.builder;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userName)
            .orElseGet(() -> userRepository.findById(userName)
                .orElseThrow(NotFoundUserException::new));

        return builder()
            .username(user.getId())
            .password(user.getPassword())
            .authorities(user.getRole().name())
            .build();
    }
}
