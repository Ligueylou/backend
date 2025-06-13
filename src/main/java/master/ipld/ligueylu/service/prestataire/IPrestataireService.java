package master.ipld.ligueylu.service.prestataire;

import master.ipld.ligueylu.model.*;
import master.ipld.ligueylu.request.AddPrestataireRequest;
import master.ipld.ligueylu.request.UpdatePrestataireRequest;

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
    List<Prestataire> searchBySpecialite(String nomSpecialite);
    List<Prestataire> findByAdresse(String villeOuRegion);
    List<Prestataire> findByScoreGreaterThan(double minScore);
    void updateScore(Long prestataireId, double newScore);
    double getScore(Long prestataireId);
    Adresse getAdresse(Long prestataireId);
    void updateAdresse(Long prestataireId, Adresse adresse);
    Map<String, Long> countPrestatairesBySpecialite();
    Set<Specialite> getSpecialites(Long prestataireId);
    void addSpecialite(Long prestataireId, Specialite specialite);
    void removeSpecialite(Long prestataireId, Long specialiteId);
    List<Service> getServicesByPrestataire(Long prestataireId);
    Service addServiceToPrestataire(Long prestataireId, Service service);
    void removeServiceFromPrestataire(Long prestataireId, Long serviceId);
    List<Reservation> getReservationsByPrestataire(Long prestataireId);
    void cancelReservation(Long prestataireId, Long reservationId);
    Reservation addReservationToPrestataire(Long prestataireId, Reservation reservation);
}
