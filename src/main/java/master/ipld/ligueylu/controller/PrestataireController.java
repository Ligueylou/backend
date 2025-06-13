package master.ipld.ligueylu.controller;

import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.exception.ResourceAlreadyExistException;
import master.ipld.ligueylu.exception.ResourceNotFoundException;
import master.ipld.ligueylu.model.Prestataire;
import master.ipld.ligueylu.request.AddPrestataireRequest;
import master.ipld.ligueylu.request.UpdatePrestataireRequest;
import master.ipld.ligueylu.response.ApiResponse;
import master.ipld.ligueylu.service.prestataire.IPrestataireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/prestataires")
public class PrestataireController {
    private final IPrestataireService prestataireService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllAdresses() {
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

}
