package podium.lib.util.errorhandling;

import java.util.List;
import java.util.stream.Collectors;

/*
 * Copyright(c) Lendlease Corporation, all rights reserved
 */

public class ValidationException extends RuntimeException {
  
  private static final long serialVersionUID = 5651118834261936746L;

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(List<String> missingFields) {
    this(missingFields != null ? toString(missingFields) : null);
  }
  
  private static String toString(List<String> missingFields) {
    return missingFields.stream().map(error -> error + " is missing.").collect(Collectors.toList()).toString();
  }

}
