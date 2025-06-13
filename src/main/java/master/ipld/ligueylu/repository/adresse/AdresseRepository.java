package master.ipld.ligueylu.repository.adresse;

import jakarta.validation.constraints.NotBlank;
import master.ipld.ligueylu.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {
    List<Adresse> findByVille(@Param("ville") String ville);

    List<Adresse> findByVilleAndRue(@NotBlank String ville, String rue);
    Optional<Adresse> findByNumeroAndRueAndVilleAndCodePostalAndPays(
            int numero, String rue, String ville, String codePostal, String pays
    );

}
