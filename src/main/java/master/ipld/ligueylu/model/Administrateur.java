package master.ipld.ligueylu.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import master.ipld.ligueylu.model.abstracts.Utilisateur;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Administrateur extends Utilisateur {
    private String codeSecret;
    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "administrateur" , orphanRemoval = true)
    @JsonIgnore
    private List<Prestataire> prestataires;
}
