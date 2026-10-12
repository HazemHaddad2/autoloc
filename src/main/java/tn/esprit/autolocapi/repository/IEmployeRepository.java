
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Employe;

public interface IEmployeRepository extends JpaRepository<Employe, Long> {
}
