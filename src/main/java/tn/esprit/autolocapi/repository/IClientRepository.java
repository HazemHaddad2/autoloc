
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Client;

public interface IClientRepository extends JpaRepository<Client, Long> {
}
