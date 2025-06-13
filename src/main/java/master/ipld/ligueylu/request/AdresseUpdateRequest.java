package master.ipld.ligueylu.request;

import lombok.Data;

@Data
public class AdresseUpdateRequest {
    private Long id;
    private int numero;
    private String ville;
    private String rue;
    private String codePostal;
    private String pays;
}
