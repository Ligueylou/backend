package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Specialite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "le libelle est obligatoire")
    private String libelle;
    @Lob
    private String description;
    @Min(value = 0, message = "L'annee d'experience doit etre positive")
    private int anneeExperience;

    @Lob
    private byte[] certification;

    @ManyToMany(mappedBy = "specialites")
    @JsonIgnore
    private Set<Prestataire> prestataires = new HashSet<>();
}
