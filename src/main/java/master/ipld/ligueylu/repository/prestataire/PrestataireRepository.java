package master.ipld.ligueylu.repository.prestataire;

import master.ipld.ligueylu.model.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrestataireRepository extends JpaRepository<Prestataire, Long>
{
    Optional<Prestataire> findByEmail(String email);
    Optional<Prestataire> findByIdAndActifTrue(Long id);
    @Query("SELECT p FROM Prestataire p JOIN p.specialites s WHERE LOWER(s.libelle) LIKE LOWER(CONCAT('%', :nomSpecialite, '%'))")
    List<Prestataire> findByNomSpecialite(@Param("nomSpecialite") String nomSpecialite);
    @Query("SELECT s.libelle, COUNT(p) FROM Prestataire p JOIN p.specialites s GROUP BY s.libelle")
    List<Object[]> countPrestataireBySpecialites();
}
