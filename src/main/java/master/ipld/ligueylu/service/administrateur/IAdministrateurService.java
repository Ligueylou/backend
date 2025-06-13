package master.ipld.ligueylu.service.administrateur;

import master.ipld.ligueylu.model.Administrateur;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAdministrateurService {
    Administrateur addAdministrateur(Administrateur administrateur);
    List<Administrateur> getAllAdministrateurs();
    Administrateur getAdministrateur(int id);
    ResponseEntity<Administrateur> updateAdministrateur(Administrateur administrateur);
    ResponseEntity<Void> deleteAdministrateur(int id);
}
