package master.ipld.ligueylu.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    private boolean success;
    private String message;
    private Object data;
}
