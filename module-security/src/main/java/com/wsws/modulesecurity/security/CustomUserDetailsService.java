package com.wsws.modulesecurity.security;


import com.wsws.moduledomain.user.User;
import com.wsws.moduledomain.user.repo.UserRepository;
import com.wsws.moduledomain.user.vo.Email;
import com.wsws.modulesecurity.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(Email.from(email))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        return UserPrincipalMapper.fromDomain(user);
    }
}
