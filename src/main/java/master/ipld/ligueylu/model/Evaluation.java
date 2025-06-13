package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DecimalMin(value = "0.0", inclusive = true, message = "Le score doit être positif")
    private double score;
    @Size(max = 500, message = "Le commentaire ne doit pas dépasser 500 caractères.")
    private String commentaire;


    @OneToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private Service service;
}
