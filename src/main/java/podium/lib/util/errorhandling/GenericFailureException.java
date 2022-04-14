package podium.lib.util.errorhandling;

public class GenericFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GenericFailureException(String message) {
		super(message);
	}

	public GenericFailureException(String message, Exception ex) {
		super(message, ex);
	}
}
