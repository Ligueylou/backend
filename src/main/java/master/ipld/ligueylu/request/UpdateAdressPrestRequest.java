package master.ipld.ligueylu.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.model.Adresse;

@Data
@RequiredArgsConstructor
public class UpdateAdressPrestRequest {
    private Long prestataireId;
    private Adresse adresse;
}
