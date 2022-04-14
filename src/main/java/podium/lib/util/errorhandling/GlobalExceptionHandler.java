package podium.lib.util.errorhandling;

import java.util.NoSuchElementException;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ControllerAdvice triggers only when exceptions are thrown from controllers
 */
@Slf4j
@ControllerAdvice
@PropertySource(value = "classpath:app-custom-error-message.properties")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@Value("${pattern-syntax-exception}")
	private String patternSyntaxException;
	
	@Value("${bad-sql-grammar-exception}")
	private String badSqlGrammarException;
	
	@Value("${data-integrity-violation-exception}")
	private String dataIntegrityViolationException;
	
	@Value("${data-access-exception}")
	private String dataAccessException;
	
	@Value("${http-message-not-readable-exception}")
	private String httpMessageNotReadableException;
	
	@Value("${appservice-code}")
	private String appServiceCode;
	
	@Value("${code-message-separator}")
	private String separator;
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ErrorDetails> handleMethodNotFound(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails body = buildErrorDetails(HttpStatus.NOT_FOUND, getReadableMessage(ex), 100);
		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PatternSyntaxException.class)
	public ResponseEntity<ErrorDetails> globalPatternSyntaxExceptionHandler(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, patternSyntaxException, 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadSqlGrammarException.class)
	public ResponseEntity<ErrorDetails> globalBadSqlGrammarExceptionHandler(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, badSqlGrammarException, 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorDetails> globalDataIntegrityViolationExceptionHandler(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, dataIntegrityViolationException, 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorDetails> globalDataAccessExceptionHandler(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, dataAccessException, 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(GenericFailureException.class)
	public ResponseEntity<ErrorDetails> globalGenericFailureExceptionHandler(Exception ex, WebRequest request) {
		ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 100);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessException.class)
	public final ResponseEntity<ErrorDetails> handlerSecurityForbidden(Exception ex, WebRequest request) {
		ErrorDetails body = buildErrorDetails(HttpStatus.FORBIDDEN, getReadableMessage(ex), 100);
		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(JsonParsingException.class)
	public final ResponseEntity<ErrorDetails> handlerJsonParsingException(Exception ex, WebRequest request) {
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, getReadableMessage(ex), 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler({ IllegalStateException.class, IllegalArgumentException.class, ParameterMissingException.class })
	public final ResponseEntity<ErrorDetails> handle(Exception ex, WebRequest request) {
		ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
		ErrorDetails body = buildErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, getReadableMessage(ex), 100);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
    // when constraint violations
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 100);
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // when method param is wrong type
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
        MethodArgumentTypeMismatchException ex, WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 100);
      return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
	
	/* 
	 * Override methods of ResponseEntityExceptionHandler to handle exceptions during request binding, before reaching controllers
	 */
	// when invalid request body
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
      return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }
	  
    // when @Valid failed validation
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
      return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }
	  
    // when required param is missing
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
        MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(HttpStatus.BAD_REQUEST, getReadableMessage(ex), 150);
      return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }
	  
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
        AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {      
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }
	  
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }
	  
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
        ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }
	  
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
        HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
        MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
        ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
        HttpHeaders headers, HttpStatus status, WebRequest request) {
      ErrorDetails body = buildErrorDetails(status, getReadableMessage(ex), 100);
      return handleExceptionInternal(ex, body, headers, status, request);
    }
    
	protected ErrorDetails buildErrorDetails(HttpStatus status, String message, int code) {
		if (message != null && message.contains(separator)) {
			String[] codeMessageArray = message.split(separator);
			return ErrorDetails.of(status.value(), status.getReasonPhrase(), codeMessageArray[1],
					String.format("%s-%s", appServiceCode, codeMessageArray[0]));

		} else
			return ErrorDetails.of(status.value(), status.getReasonPhrase(), message, String.format("%s-%s", appServiceCode, code));
	}
    
    private String getReadableMessage(Exception ex) {
      return ReadableExceptionMessageParser.getReadableMessage(ex);
    }

}
