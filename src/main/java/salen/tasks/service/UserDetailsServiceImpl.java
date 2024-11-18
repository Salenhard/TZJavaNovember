package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import salen.tasks.entity.User;
import salen.tasks.entity.UserPrincipal;
import salen.tasks.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmailAndIsActive(email, true)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        return new UserPrincipal(user);
    }
}
