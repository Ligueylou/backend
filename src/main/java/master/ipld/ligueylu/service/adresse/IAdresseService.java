package master.ipld.ligueylu.service.adresse;

import master.ipld.ligueylu.model.Adresse;
import master.ipld.ligueylu.request.AddAdresseRequest;
import master.ipld.ligueylu.request.AdresseUpdateRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAdresseService {
    Adresse addAdresse(AddAdresseRequest adresse);
    Adresse getAdresseById(Long adresseId);
    List<Adresse> getAllAdresses();
    List<Adresse> getAdressesByVille(String ville);
    List<Adresse> getAdressesByVilleAndRue(String ville, String rue);
    Adresse updateAdresse(AdresseUpdateRequest adresse,Long adresseId);
    void deleteAdresse(Long id);
}
