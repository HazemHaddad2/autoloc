
package tn.esprit.autolocapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autolocapi.domain.Reservation;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {
}
