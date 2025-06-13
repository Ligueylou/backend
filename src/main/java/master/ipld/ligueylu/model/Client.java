package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Client extends Utilisateur {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adresse_id")
    @JsonIgnore
    private Adresse adresse;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "client", orphanRemoval = true)
    private List<Reservation> reservations;
}
