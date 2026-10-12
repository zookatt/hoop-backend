package zotov.hoop_backend.auth.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zotov.hoop_backend.credentials.entity.UserCredentials;
import zotov.hoop_backend.credentials.repository.UserCredentialsRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserCredentialsRepository userCredentialsRepository;

    public DatabaseUserDetailsService(UserCredentialsRepository userCredentialsRepository) {
        this.userCredentialsRepository = userCredentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentials credentials = userCredentialsRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email " + email));

        return User
                .withUsername(credentials.getEmail())
                .password(credentials.getPassword())
                .disabled(Boolean.FALSE.equals(credentials.getUser().getActive()))
                .authorities(credentials.getUser().getRole().getName())
                .build();
    }
}