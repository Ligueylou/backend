package master.ipld.ligueylu.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class ScoreUpdateRequest {
    private Long prestataireId;
    private double newScore;
}
