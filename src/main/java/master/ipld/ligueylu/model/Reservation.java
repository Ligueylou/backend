package master.ipld.ligueylu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import master.ipld.ligueylu.model.enums.Status;
import master.ipld.ligueylu.model.enums.TypeService;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String libelle;
    @Enumerated(EnumType.STRING)
    private TypeService typeService;
    @Lob
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestataire_id")
    @JsonIgnore
    private Prestataire prestataire;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Service service;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private Notification notification;

}
