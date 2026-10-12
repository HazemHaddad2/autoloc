
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Long> {
}
