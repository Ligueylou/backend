package master.ipld.ligueylu.dto;

import lombok.Data;

@Data
public class AdresseDto {
    private Long id;
    private String numero;
    private String ville;
    private String rue;
    private String codePostal;
    private String pays;
}
