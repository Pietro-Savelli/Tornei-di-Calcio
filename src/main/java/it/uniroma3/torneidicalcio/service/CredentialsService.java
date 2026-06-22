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

    private final PasswordEncoder passwordEncoder;
    private final CredentialsRepository credentialsRepository;

    public CredentialsService(PasswordEncoder passwordEncoder, CredentialsRepository credentialsRepository) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsRepository = credentialsRepository;
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(Long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional(readOnly = true)
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = Optional.ofNullable(this.credentialsRepository.findByUsername(username));
        return result.orElse(null);
    }

    @Transactional
    public void saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
        credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        this.credentialsRepository.save(credentials);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Credentials findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }

}
