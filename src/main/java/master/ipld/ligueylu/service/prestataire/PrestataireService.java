package master.ipld.ligueylu.service.prestataire;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.exception.ResourceAlreadyExistException;
import master.ipld.ligueylu.exception.ResourceNotFoundException;
import master.ipld.ligueylu.model.Adresse;
import master.ipld.ligueylu.model.Prestataire;
import master.ipld.ligueylu.model.Reservation;
import master.ipld.ligueylu.model.Specialite;
import master.ipld.ligueylu.model.enums.Role;
import master.ipld.ligueylu.repository.prestataire.PrestataireRepository;
import master.ipld.ligueylu.repository.reservation.ReservationRepository;
import master.ipld.ligueylu.repository.service.ServiceRepository;
import master.ipld.ligueylu.repository.specialite.SpecialiteRepository;
import master.ipld.ligueylu.request.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PrestataireService implements IPrestataireService {
    private final PrestataireRepository prestataireRepository;
    private final PasswordEncoder passwordEncoder;
    private final SpecialiteRepository specialiteRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Prestataire addPrestataire(AddPrestataireRequest request) {
        boolean existedPrestataire = prestataireRepository.findByEmail(request.getEmail()).isPresent();
        if (existedPrestataire) {
            throw new ResourceAlreadyExistException("Prestataire " + request.getEmail() + " already exists");
        }

        return prestataireRepository.save(createPrestataire(request));
    }
    public Prestataire createPrestataire(AddPrestataireRequest request) {
        return new Prestataire(
                request.getEmail(),
                request.getNomComplet(),
                passwordEncoder.encode(request.getPassword()),
                request.getTelephone()
        );
    }

    @Override
    public List<Prestataire> getAllPrestataire() {
        return prestataireRepository.findAll();
    }

    @Override
    public Prestataire getPrestataireByEmail(String email) {
        return prestataireRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Prestataire introuvable"));
    }

    @Override
    public Prestataire getPrestataireById(Long id) {
        return prestataireRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Prestataire introuvable"));
    }

    @Override
    public Prestataire updatePrestataire(UpdatePrestataireRequest prestataire, Long id) {
        return prestataireRepository.findById(id)
                .map(existingPrestataire -> updateExistingPrestataire(existingPrestataire,prestataire))
                .map(prestataireRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
    }

    public Prestataire updateExistingPrestataire(Prestataire existingPrestataire , UpdatePrestataireRequest request)
    {
        existingPrestataire.setEmail(request.getEmail());
        existingPrestataire.setNomComplet(request.getNomComplet());
        existingPrestataire.setPassword(passwordEncoder.encode(request.getPassword()));
        existingPrestataire.setTelephone(request.getTelephone());
        existingPrestataire.setRole(Role.PRESTATAIRE);
        return existingPrestataire;
    }

    @Override
    public void deletePrestataire(Long id) {
      prestataireRepository.findById(id)
              .ifPresentOrElse(prestataireRepository::delete,
                      () -> {throw new ResourceNotFoundException("Prestataire introuvable");});
    }

    @Override
    public Optional<Prestataire> isPrestataireActif(Long prestataireId) {
        return prestataireRepository.findByIdAndActifTrue(prestataireId);
    }

    @Override
    public boolean activatePrestataire(Long id) {
        Prestataire prestataire = prestataireRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Prestataire introuvable"));

        prestataire.setActif(true);
        prestataireRepository.save(prestataire);
        return true;
    }

    @Override
    public List<Prestataire> searchBySpecialite(String nomSpecialite) {
        if(nomSpecialite ==null || nomSpecialite.trim().isEmpty())
        {
            throw new IllegalArgumentException("Le nom de la specialite ne doit pas etre vide ");
        }
        return prestataireRepository.findByLibelleSpecialite(nomSpecialite);
    }

    @Override
    public Optional<Prestataire> findByAdresse(String ville) {
        return prestataireRepository.findByAdresse_Ville(ville);
    }

    @Override
    public List<Prestataire> findByScoreGreaterThan(double minScore) {
        return prestataireRepository.findByScoreGreaterThan(minScore);
    }

    @Override
    public void updateScore(ScoreUpdateRequest scoreUpdateRequest) {
        Prestataire prestataire = prestataireRepository.findById(scoreUpdateRequest.getPrestataireId())
                .orElseThrow(() -> new EntityNotFoundException("Prestataire introuvable avec l'id " + scoreUpdateRequest.getPrestataireId()));

        prestataire.setScore(scoreUpdateRequest.getNewScore());
        prestataireRepository.save(prestataire);
    }


    @Override
    public double getScore(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvale avec l'id"));
        return prestataire.getScore();
    }

    @Override
    public Adresse getAdresse(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        return prestataire.getAdresse();
    }

    @Override
    public void updateAdressePrestataire(UpdateAdressPrestRequest request) {
            Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
            prestataire.setAdresse(request.getAdresse());
            prestataireRepository.save(prestataire);
    }

    @Override
    public Map<String, Long> countPrestatairesBySpecialite() {
        List<Object[]> results = prestataireRepository.countPrestataireBySpecialites();
        Map<String, Long> counts = new HashMap<>();
        for(Object[] result : results) {
            String specialite = (String) result[0];
            Long count = (Long) result[1];
            counts.put(specialite, count);
        }
        return counts;
    }

    @Override
    public Set<Specialite> getSpecialitesFromPrestataire(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        return prestataire.getSpecialites();
    }

    @Override
    public void addSpecialiteToPrestataire(AddSpecialitePrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable avec l'id : " + request.getPrestataireId()));

        Specialite specialite = request.getSpecialite();

        // Rechercher la spécialité par libellé (en ignorant la casse, optionnel)
        Optional<Specialite> existingSpecialite = specialiteRepository.findByLibelleIgnoreCase(specialite.getLibelle());

        Specialite specialiteToAdd = existingSpecialite.orElseGet(() -> {
            // Préserve la cohérence relationnelle
            specialite.getPrestataires().add(prestataire);
            return specialiteRepository.save(specialite);
        });

        prestataire.getSpecialites().add(specialiteToAdd);
        prestataireRepository.save(prestataire);
    }


    @Override
    public void removeSpecialiteToPrestataire(AddSpecialitePrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));

        Specialite specialite = specialiteRepository.findById(request.getSpecialite().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialite introuvable"));
        prestataire.getSpecialites().remove(specialite);
        prestataireRepository.save(prestataire);
    }

    @Override
    public List<master.ipld.ligueylu.model.Service> getServicesByPrestataire(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        return prestataire.getServices();
    }

    @Override
    public master.ipld.ligueylu.model.Service addServiceToPrestataire(AddServicePrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable avec l'id : " + request.getPrestataireId()));

        master.ipld.ligueylu.model.Service service = request.getService();
        master.ipld.ligueylu.model.Service serviceToAdd;

        if (service.getId() != null) {
            serviceToAdd = serviceRepository.findById(service.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Service introuvable avec l'id : " + service.getId()));
        } else {
            service.setPrestataire(prestataire); // Lier le prestataire au service
            serviceToAdd = serviceRepository.save(service);
        }
        prestataire.getServices().add(serviceToAdd);
        prestataireRepository.save(prestataire);

        return serviceToAdd;
    }


    @Override
    public void removeServiceFromPrestataire(AddServicePrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        master.ipld.ligueylu.model.Service service = serviceRepository.findById(request.getService().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Service introuvable"));
        prestataire.getServices().remove(service);
        prestataireRepository.save(prestataire);
    }

    @Override
    public List<Reservation> getReservationsByPrestataire(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        return prestataire.getReservations();
    }

    @Override
    public void cancelReservation(AddReservationPrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        Reservation reservation = reservationRepository.findById(request.getReservation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable"));
        prestataire.getReservations().remove(reservation);
        prestataireRepository.save(prestataire);
    }

    @Override
    public Reservation addReservationToPrestataire(AddReservationPrestRequest request) {
        Prestataire prestataire = prestataireRepository.findById(request.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable avec l'id : " + request.getPrestataireId()));

        Reservation reservation = request.getReservation();
        Reservation reservationToAdd;
        if (reservation.getId() != null) {
            reservationToAdd = reservationRepository.findById(reservation.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable avec l'id : " + reservation.getId()));
        } else {
            reservation.setPrestataire(prestataire);
            if (reservation.getCreationDate() == null) {
                reservation.setCreationDate(new Date());
            }

            reservationToAdd = reservationRepository.save(reservation);
        }
        prestataire.getReservations().add(reservationToAdd);
        prestataireRepository.save(prestataire);

        return reservationToAdd;
    }

}
