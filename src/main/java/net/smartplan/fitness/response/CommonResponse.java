package net.smartplan.fitness.response;

import lombok.Data;

/**
 * @author H.D. Sachin Dilshan
 */

@Data
public class CommonResponse {

    private boolean success;
    private final String message;

    public CommonResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
