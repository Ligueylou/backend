package master.ipld.ligueylu.controller;

import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.exception.ResourceAlreadyExistException;
import master.ipld.ligueylu.exception.ResourceNotFoundException;
import master.ipld.ligueylu.model.Prestataire;
import master.ipld.ligueylu.model.Specialite;
import master.ipld.ligueylu.request.*;
import master.ipld.ligueylu.response.ApiResponse;
import master.ipld.ligueylu.service.prestataire.IPrestataireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/prestataires")
public class PrestataireController {
    private final IPrestataireService prestataireService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPrestataires() {
        try {
            List<Prestataire> prestataires = prestataireService.getAllPrestataire();
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Liste des prestataires : ",
                    prestataires
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                    false,
                    "Error",
                    e.getMessage()
            ));
        }
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getPrestataireByEmail(@PathVariable String email) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireByEmail(email);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire trouvé !",
                    prestataire
            ));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPrestataireById(@PathVariable Long id) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire trouvé !",
                    prestataire
            ));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @PostMapping
    public ResponseEntity<ApiResponse> addPrestataire(@RequestBody AddPrestataireRequest prestataire) {
        try{
            Prestataire prestataireResult = prestataireService.addPrestataire(prestataire);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire ajouté avec succes",
                    prestataireResult
            ));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
        catch (ResourceAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                    false,
                    "Error",
                    e.getMessage()
            ));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePrestataire(@RequestBody UpdatePrestataireRequest prestataire, @PathVariable Long id) {
        try {
            Prestataire prestataireResult = prestataireService.updatePrestataire(prestataire,id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire mis a jour avec succes ! ",
                    prestataireResult
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(
                    false,
                    "Error",
                    e.getMessage()
            ));
        }
    }
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> deletePrestataire(@PathVariable Long id) {
        try {
            prestataireService.deletePrestataire(id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire supprimé avec succes ! ",
                    id
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }

    @GetMapping("/stats/specialites")
    public ResponseEntity<ApiResponse> countBySpecialite() {
        Map<String, Long> data = prestataireService.countPrestatairesBySpecialite();
        return ResponseEntity.ok(new ApiResponse(true, "Statistiques par spécialité", data));
    }
    @GetMapping("/actif/{id}")
    public ResponseEntity<ApiResponse> isPrestataireActif(@PathVariable Long id) {
        Optional<Prestataire> prestataireOpt = prestataireService.isPrestataireActif(id);

        if (prestataireOpt.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Le prestataire est actif.",
                    prestataireOpt
            ));
        } else {
            return ResponseEntity.ok(new ApiResponse(
                    false,
                    "Le prestataire est inactif.",
                    false
            ));
        }
    }

    @GetMapping("/activate/{id}")
    public ResponseEntity<ApiResponse> activatePrestataire(@PathVariable Long id) {
        try {
            Prestataire prestataire = prestataireService.getPrestataireById(id);
            boolean activated = prestataireService.activatePrestataire(id);

            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Prestataire activé avec succès",
                    prestataire
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @GetMapping("/search/{specialite}")
    public ResponseEntity<ApiResponse> searchPrestataireBySpecialite(@PathVariable String specialite) {
        List<Prestataire> prestataires = prestataireService.searchBySpecialite(specialite);

        if (prestataires.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Aucun prestataire trouvé pour la spécialité : " + specialite,
                    prestataires
            ));
        }

        return ResponseEntity.ok(new ApiResponse(
                true,
                "Liste des prestataires pour la spécialité : " + specialite,
                prestataires
        ));
    }
    @GetMapping("/search/adresses/{ville}")
    public ResponseEntity<ApiResponse> searchPrestataireByVille(@PathVariable String ville) {
        Optional<Prestataire> prestataires = prestataireService.findByAdresse(ville);
        if(prestataires.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Aucun prestataire trouvé pour l'adresse : " + ville,
                    prestataires
            ));
        }
        return ResponseEntity.ok(new ApiResponse(
                true,
                "Liste des prestataires pour l'adresse : " + ville,
                prestataires
        ));
    }
    @GetMapping("/search/score/{score}")
    public ResponseEntity<ApiResponse> findByScoreGreaterThan(@PathVariable double score) {
        List<Prestataire> prestataires = prestataireService.findByScoreGreaterThan(score);
        if (prestataires.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Aucun prestataire trouvé avec un score : " + score,
                    prestataires
            ));

        }
        return ResponseEntity.ok(new ApiResponse(
                true,
                "Liste des prestataires avec un score :"+score,
                prestataires
        ));
    }
    @PutMapping("/score/")
    public ResponseEntity<ApiResponse> updatePrestataireScore(@RequestBody ScoreUpdateRequest request) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.updateScore(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Score mis a jour avec success",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @PutMapping("/adress/update/")
    public ResponseEntity<ApiResponse> updatePrestataireAdresse(@RequestBody UpdateAdressPrestRequest request) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.updateAdressePrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Adresse du prestatataire mis a jour avec success",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @PostMapping("/specialite/add/")
    public ResponseEntity<ApiResponse> addPrestataireSpecialite(@RequestBody AddSpecialitePrestRequest request) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.addSpecialiteToPrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Ajout avec success de la specialite",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }

    }
    @DeleteMapping("/specialite/remove/")
    public ResponseEntity<ApiResponse> removePrestataireSpecialite(@RequestBody AddSpecialitePrestRequest request)
    {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.removeSpecialiteToPrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Specialite supprimé avec success :",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }


    }
    @GetMapping("/specialite/prestataire/{id}")
    public ResponseEntity<ApiResponse> getSpecialiteFromPrestataire(@PathVariable Long id){
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(id);
            Set<Specialite> specialite = prestataireService.getSpecialitesFromPrestataire(id);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Liste des specialites : ",
                    specialite
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }
    }
    @PostMapping("/service/add/")
    public ResponseEntity<ApiResponse> addPrestataireService(@RequestBody AddServicePrestRequest request) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.addServiceToPrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Ajout avec success du service",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }

    }
    @DeleteMapping("/specialite/cancel/")
    public ResponseEntity<ApiResponse> removePrestataireService(@RequestBody AddServicePrestRequest request)
    {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.removeServiceFromPrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Service quitter avec success",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }


    }
    @PostMapping("/reservation/add/")
    public ResponseEntity<ApiResponse> addPrestataireReservation(@RequestBody AddReservationPrestRequest request) {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.addReservationToPrestataire(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Ajout avec success de la reservation",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }

    }
    @DeleteMapping("/reservation/cancel/")
    public ResponseEntity<ApiResponse> removePrestataireReservation(@RequestBody AddReservationPrestRequest request)
    {
        try{
            Prestataire prestataire = prestataireService.getPrestataireById(request.getPrestataireId());
            prestataireService.cancelReservation(request);
            return ResponseEntity.ok(new ApiResponse(
                    true,
                    "Ajout avec success de la reservation",
                    prestataire
            ));
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(
                    false,
                    e.getMessage(),
                    null
            ));
        }


    }

}
