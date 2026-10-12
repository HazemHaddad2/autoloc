
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
}
