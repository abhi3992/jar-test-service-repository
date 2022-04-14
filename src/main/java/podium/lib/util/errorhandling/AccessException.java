package podium.lib.util.errorhandling;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
public class AccessException extends RuntimeException {
	private static final long serialVersionUID = 7808937290245236633L;
	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
