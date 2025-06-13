package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ipld.ligueylu.model.enums.MethodePaiement;
import master.ipld.ligueylu.model.enums.Status;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

//Transaction @Transactional
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DecimalMin(value = "0.0", inclusive = true, message = "Le montant doit etre positif")
    private double montant;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private MethodePaiement methode;

    @OneToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private Service service;

    @OneToOne(cascade = CascadeType.ALL , mappedBy = "paiement")
    private Notification notification;
}
