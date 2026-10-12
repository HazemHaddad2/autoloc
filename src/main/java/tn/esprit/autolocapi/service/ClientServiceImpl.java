package tn.esprit.autolocapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autolocapi.domain.Client;
import tn.esprit.autolocapi.exception.ResourceNotFoundException;
import tn.esprit.autolocapi.repository.IClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        verifier(client);

        if (client.getIdClient() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau client ne doit pas avoir d'identifiant"
            );
        }

        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", id)
                );
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        Client existant = findById(id);
        verifier(client);

        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setTelephone(client.getTelephone());
        existant.setNumPermis(client.getNumPermis());
        existant.setDateInscription(client.getDateInscription());

        return clientRepository.save(existant);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.delete(findById(id));
    }

    private void verifier(Client client) {
        if (client.getNom() == null || client.getNom().isBlank()
                || client.getPrenom() == null
                || client.getPrenom().isBlank()
                || client.getEmail() == null
                || client.getEmail().isBlank()
                || client.getNumPermis() == null
                || client.getNumPermis().isBlank()
                || client.getDateInscription() == null) {
            throw new IllegalArgumentException(
                    "Les informations obligatoires du client doivent etre renseignees"
            );
        }
    }
}
