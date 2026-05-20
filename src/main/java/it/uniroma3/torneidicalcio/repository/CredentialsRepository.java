package it.uniroma3.torneidicalcio.repository;

import it.uniroma3.torneidicalcio.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    Credentials findByUsername(String username);

}
