package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.SuperBuilder;
import master.ipld.ligueylu.model.abstracts.Utilisateur;
import master.ipld.ligueylu.model.enums.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
public class Prestataire extends Utilisateur {
    @DecimalMin(value = "0.0", inclusive = true, message = "Le score doit être positif")
    private double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adresse_id")
    @JsonIgnore
    private Adresse adresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private Administrateur administrateur;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "specialite_prestataire", joinColumns = {
            @JoinColumn(name = "specialite_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")})
    @JsonIgnore
    private Set<Specialite> specialites = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "prestataire",orphanRemoval = true)
    @JsonIgnore
    private List<Service> services;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "prestataire",orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    public Prestataire(String email, String nomComplet, String password, String telephone) {
        super();
        this.setEmail(email);
        this.setNomComplet(nomComplet);
        this.setPassword(password);
        this.setTelephone(telephone);
        this.setRole(Role.PRESTATAIRE);
    }
}
