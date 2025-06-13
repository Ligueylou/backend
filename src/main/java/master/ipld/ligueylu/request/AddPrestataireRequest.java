package master.ipld.ligueylu.request;

import lombok.Data;
import master.ipld.ligueylu.model.enums.Role;

@Data
public class AddPrestataireRequest {
    private String nomComplet;
    private String email;
    private String password;
    private String telephone;
}
