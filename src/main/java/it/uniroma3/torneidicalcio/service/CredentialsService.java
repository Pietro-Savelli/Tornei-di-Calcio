package it.uniroma3.torneidicalcio.service;

import it.uniroma3.torneidicalcio.model.Credentials;
import it.uniroma3.torneidicalcio.model.Utente;
import it.uniroma3.torneidicalcio.repository.CredentialsRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CredentialsService {

    private PasswordEncoder passwordEncoder;
    private CredentialsRepository credentialsRepository;

    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = Optional.ofNullable(this.credentialsRepository.findByUsername(username));
        return result.orElse(null);
    }

    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    public Utente getUtenteCorrente(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();
            Credentials credentials = this.findByUsername(username);

            if (credentials != null) {
                return credentials.getUtente();
            }
        }
        return null;
    }

    public Credentials findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }

}
