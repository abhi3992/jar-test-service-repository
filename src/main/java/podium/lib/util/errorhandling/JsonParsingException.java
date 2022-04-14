package podium.lib.util.errorhandling;

public class JsonParsingException extends RuntimeException {
	private static final long serialVersionUID = -7349544501502166330L;

	public JsonParsingException(String message) {
		super(message);
	}

	public JsonParsingException(String message, Throwable ex) {
		super(message, ex);
	}

	public JsonParsingException(Exception ex) {
		super(ex);
	}
}
