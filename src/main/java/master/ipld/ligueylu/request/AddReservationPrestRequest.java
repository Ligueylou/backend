package master.ipld.ligueylu.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import master.ipld.ligueylu.model.Reservation;

@Data
@RequiredArgsConstructor
public class AddReservationPrestRequest {
    private Long prestataireId;
    private Reservation reservation;
}
