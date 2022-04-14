package podium.lib.util.errorhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterMissingException extends RuntimeException {  

	private static final long serialVersionUID = 1L;

	public ParameterMissingException(String message) {  
		super(message);  
	} 
	
	public ParameterMissingException(String message, Exception ex) {
		super(message, ex);
	}

}
