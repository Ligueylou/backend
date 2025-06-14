package master.ipld.ligueylu.repository.specialite;

import jakarta.validation.constraints.NotBlank;
import master.ipld.ligueylu.model.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
    Optional<Specialite> findByLibelle(@NotBlank(message = "le libelle est obligatoire") String libelle);
}
