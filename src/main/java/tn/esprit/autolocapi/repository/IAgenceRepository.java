
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Agence;

public interface IAgenceRepository extends JpaRepository<Agence, Long> {
}
