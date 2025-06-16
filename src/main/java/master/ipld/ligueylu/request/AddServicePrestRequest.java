package master.ipld.ligueylu.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.model.Service;

@Data
@RequiredArgsConstructor
public class AddServicePrestRequest {
    private Long prestataireId;
    private Service service;
}
