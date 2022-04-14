package podium.lib.util.errorhandling;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDetails {
    
    private Instant timestamp;
  
	private int status;

	private String error;

	private String message;

	private String code;

	public ErrorDetails(String message) {
		this.message = message;
		timestamp = Instant.now();
	}

	public ErrorDetails(int status, String error, String message) {
		this.message = message;
		this.status = status;
		this.error = error;
		timestamp = Instant.now();
	}
	
	public ErrorDetails(int status, String error, String message, String code) {
	  this.message = message;
      this.status = status;
      this.error = error;
      this.code = code;
      timestamp = Instant.now();
	}

	public static ErrorDetails of(String message) {
		return new ErrorDetails(message);
	}

	public static ErrorDetails of(int status, String error, String message) {
		return new ErrorDetails(status, error, message);
	}
	
	public static ErrorDetails of(int status, String error, String message, String code) {
      return new ErrorDetails(status, error, message, code);
  }

}