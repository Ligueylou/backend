package master.ipld.ligueylu.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.model.Specialite;

@RequiredArgsConstructor
@Data
public class AddSpecialitePrestRequest {
    private Long prestataireId;
    private final Specialite specialite;
}
