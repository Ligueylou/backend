package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ipld.ligueylu.model.enums.TypeService;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TypeService typeService;
    private int duree;
    private double tarifStandard;
    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestataire_id")
    @JsonIgnore
    private Prestataire prestataire;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    @JsonIgnore
    private Reservation reservation;

    @OneToOne(mappedBy = "service", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Paiement paiement;

    @OneToOne(mappedBy = "service" , cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Evaluation evaluation;
}
