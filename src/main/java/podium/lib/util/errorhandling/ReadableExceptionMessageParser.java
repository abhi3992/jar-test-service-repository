package podium.lib.util.errorhandling;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/*
 * Copyright(c) Lendlease Corporation, all rights reserved
 */

// This class handles the parsing of various exceptions and tries to build a readable error message.
// Fallbacks to exception.getMessage() when the class is not specifically handled.
public class ReadableExceptionMessageParser {
  
  public static final String ENUM_ARGUMENT_MISMATCH_MSG = "%s should be one of %s.";
  
  public static final String ARGUMENT_MISMATCH_MSG = "%s should be of type %s.";
  
  private ReadableExceptionMessageParser() {
    // empty
  }
  
  public static String getReadableMessage(Exception ex) {
    if (ex instanceof ConstraintViolationException) {
      return getConstraintViolationExceptionMessage((ConstraintViolationException) ex);
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      return getMethodArgumentTypeMismatchExceptionMessage((MethodArgumentTypeMismatchException) ex);
    } else if (ex instanceof HttpMessageNotReadableException) {
      if (((HttpMessageNotReadableException) ex).getMostSpecificCause() instanceof InvalidFormatException) {
        return getInvalidFormatExceptionMessage((InvalidFormatException) ((HttpMessageNotReadableException) ex).getMostSpecificCause());
      }
    } else if (ex instanceof NoSuchElementException) {
      return ex.getMessage();
    } else if (ex instanceof ValidationException) {
      return ex.getMessage();
    } else if (ex instanceof MethodArgumentNotValidException) {
      return getMethodArgumentNotValidExceptionMessage((MethodArgumentNotValidException) ex);
    }
    
    return ex.getMessage();
  }
  
  private static String getConstraintViolationExceptionMessage(ConstraintViolationException e) {
    return e.getConstraintViolations()
        .stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.toList()).toString();
  }
  
  private static String getMethodArgumentTypeMismatchExceptionMessage(MethodArgumentTypeMismatchException e) {
    Class<?> clazz = e.getRequiredType();
    if (clazz != null) {
      if (clazz.isEnum()) {
        List<String> enumValues = Stream.of(clazz.getEnumConstants()).map(value -> value.toString()).collect(Collectors.toList());
        return String.format(ENUM_ARGUMENT_MISMATCH_MSG, e.getName(), enumValues);
      } else {
        return String.format(ARGUMENT_MISMATCH_MSG, e.getName(), clazz.getSimpleName());
      }
    }
    return e.getMessage();
  }
  
  private static final Pattern ENUM_MSG = Pattern.compile("not one of the values accepted for Enum class: (\\[([^()]*)\\])");
  private static String getInvalidFormatExceptionMessage(InvalidFormatException e) {
    Matcher match = ENUM_MSG.matcher(e.getMessage());
    if (match.find()) {
      return String.format(ENUM_ARGUMENT_MISMATCH_MSG, e.getValue().toString(), match.group(1));
    }
    return e.getMessage();
  }
  
  private static String getMethodArgumentNotValidExceptionMessage(MethodArgumentNotValidException e) {
    return e.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getDefaultMessage()).collect(Collectors.toList()).toString();
  }
  
}
