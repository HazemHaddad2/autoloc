package tn.esprit.autolocapi.service;

import tn.esprit.autolocapi.domain.Paiement;
import java.util.List;

public interface IPaiementService {
    Paiement findById(Long id);
    List<Paiement> findAll();
}
