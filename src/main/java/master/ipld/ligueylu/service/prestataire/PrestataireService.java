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
import master.ipld.ligueylu.request.AddPrestataireRequest;
import master.ipld.ligueylu.request.ScoreUpdateRequest;
import master.ipld.ligueylu.request.UpdatePrestataireRequest;
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
    public void updateAdresse(Long prestataireId, Adresse adresse) {
            Prestataire prestataire = prestataireRepository.findById(prestataireId)
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
            prestataire.setAdresse(adresse);
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
    public Set<Specialite> getSpecialites(Long prestataireId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        return prestataire.getSpecialites();
    }

    @Override
    public void addSpecialite(Long prestataireId, Specialite specialite) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        Optional<Specialite> existingSpecialite = specialiteRepository.findByLibelle(specialite.getLibelle());
        Specialite specialiteToAdd = existingSpecialite.orElseGet(() -> specialiteRepository.save(specialite));
        prestataire.getSpecialites().add(specialiteToAdd);
        prestataireRepository.save(prestataire);

    }

    @Override
    public void removeSpecialite(Long prestataireId, Long specialiteId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));

        Specialite specialite = specialiteRepository.findById(specialiteId)
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
    public master.ipld.ligueylu.model.Service addServiceToPrestataire(Long prestataireId, master.ipld.ligueylu.model.Service service) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        Optional<master.ipld.ligueylu.model.Service> existingService = serviceRepository.findById(service.getId());
        master.ipld.ligueylu.model.Service serviceToAdd = existingService.orElseGet(() -> serviceRepository.save(service));
        prestataire.getServices().add(serviceToAdd);
        prestataireRepository.save(prestataire);
        return serviceToAdd;
    }

    @Override
    public void removeServiceFromPrestataire(Long prestataireId, Long serviceId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        master.ipld.ligueylu.model.Service service = serviceRepository.findById(serviceId)
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
    public void cancelReservation(Long prestataireId, Long reservationId) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable"));
        prestataire.getReservations().remove(reservation);
        prestataireRepository.save(prestataire);
    }

    @Override
    public Reservation addReservationToPrestataire(Long prestataireId, Reservation reservation) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire introuvable"));
        Optional<Reservation> existingReservation = reservationRepository.findById(reservation.getId());
        Reservation reservationToAdd = existingReservation.orElseGet(() -> reservationRepository.save(reservation));
        prestataire.getReservations().add(reservationToAdd);
        prestataireRepository.save(prestataire);
        return reservationToAdd;
    }
}
