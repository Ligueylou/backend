package master.ipld.ligueylu.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import master.ipld.ligueylu.model.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nomComplet;
    private String email;
    private String password;
    private String telephone;
    private Role role;
}
