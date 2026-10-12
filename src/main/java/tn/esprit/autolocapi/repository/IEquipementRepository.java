
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Equipement;

public interface IEquipementRepository extends JpaRepository<Equipement, Long> {
}
