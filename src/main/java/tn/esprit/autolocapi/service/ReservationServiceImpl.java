package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Reservation;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;

    @Override
    public Reservation create(Reservation reservation) {
        verifier(reservation);

        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle reservation ne doit pas avoir d'identifiant"
            );
        }

        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation", id)
                );
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        Reservation existante = findById(id);
        verifier(reservation);

        existante.setDateDebut(reservation.getDateDebut());
        existante.setDateFin(reservation.getDateFin());
        existante.setStatut(reservation.getStatut());
        existante.setClient(reservation.getClient());
        existante.setVehicule(reservation.getVehicule());

        return reservationRepository.save(existante);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.delete(findById(id));
    }

    private void verifier(Reservation reservation) {
        if (reservation.getDateDebut() == null
                || reservation.getDateFin() == null
                || reservation.getDateFin()
                .isBefore(reservation.getDateDebut())
                || reservation.getStatut() == null) {
            throw new IllegalArgumentException(
                    "Les dates ou le statut de la reservation sont invalides"
            );
        }
    }
}
