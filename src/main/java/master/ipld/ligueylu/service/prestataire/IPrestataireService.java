package master.ipld.ligueylu.service.prestataire;

import master.ipld.ligueylu.model.*;
import master.ipld.ligueylu.request.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface IPrestataireService {
    Prestataire addPrestataire(AddPrestataireRequest prestataire);
    List<Prestataire> getAllPrestataire();
    Prestataire getPrestataireByEmail(String email);
    Prestataire getPrestataireById(Long id);
    Prestataire updatePrestataire(UpdatePrestataireRequest prestataire, Long id);
    void deletePrestataire(Long id);
    Optional<Prestataire> isPrestataireActif(Long prestataireId);
    boolean activatePrestataire(Long id);
    List<Prestataire> searchBySpecialite(String nomSpecialite);
    Optional<Prestataire> findByAdresse(String ville);
    List<Prestataire> findByScoreGreaterThan(double minScore);
    void updateScore(ScoreUpdateRequest scoreUpdateRequest);

    double getScore(Long prestataireId);
    Adresse getAdresse(Long prestataireId);
    void updateAdressePrestataire(UpdateAdressPrestRequest updateAdressPrestRequest);
    Map<String, Long> countPrestatairesBySpecialite();
    Set<Specialite> getSpecialitesFromPrestataire(Long prestataireId);
    void addSpecialiteToPrestataire(AddSpecialitePrestRequest request);
    void removeSpecialiteToPrestataire(AddSpecialitePrestRequest request);
    List<Service> getServicesByPrestataire(Long prestataireId);
    Service addServiceToPrestataire(AddServicePrestRequest request);
    void removeServiceFromPrestataire(AddServicePrestRequest request);
    List<Reservation> getReservationsByPrestataire(Long prestataireId);
    void cancelReservation(AddReservationPrestRequest request);
    Reservation addReservationToPrestataire(AddReservationPrestRequest request);
}
