package kth.id2007.project.model;

import java.util.List;
import java.util.Map;

/**
 * @author Kim Hammar on 2019-05-18.
 */
public class ParsedErrorsDTO {

  private Map<String, List<ValidationErrorMessage>> idMustBeUniqueErrors;
  private List<ValidationErrorMessage> expectedTotalElapsedTimeErrors;
  private List<ValidationErrorMessage> expectedTotalTimeErrors;
  private List<ValidationErrorMessage> unknownErrors;

  public ParsedErrorsDTO(
      Map<String, List<ValidationErrorMessage>> idMustBeUniqueErrors, List<ValidationErrorMessage> expectedTotalElapsedTimeErrors,
      List<ValidationErrorMessage> expectedTotalTimeErrors, List<ValidationErrorMessage> unknownErrors) {
    this.idMustBeUniqueErrors = idMustBeUniqueErrors;
    this.expectedTotalElapsedTimeErrors = expectedTotalElapsedTimeErrors;
    this.expectedTotalTimeErrors = expectedTotalTimeErrors;
  }

  public ParsedErrorsDTO() {
  }

  public Map<String, List<ValidationErrorMessage>> getIdMustBeUniqueErrors() {
    return idMustBeUniqueErrors;
  }

  public void setIdMustBeUniqueErrors(Map<String, List<ValidationErrorMessage>> idMustBeUniqueErrors) {
    this.idMustBeUniqueErrors = idMustBeUniqueErrors;
  }

  public List<ValidationErrorMessage> getExpectedTotalElapsedTimeErrors() {
    return expectedTotalElapsedTimeErrors;
  }

  public void setExpectedTotalElapsedTimeErrors(List<ValidationErrorMessage> expectedTotalElapsedTimeErrors) {
    this.expectedTotalElapsedTimeErrors = expectedTotalElapsedTimeErrors;
  }

  public List<ValidationErrorMessage> getExpectedTotalTimeErrors() {
    return expectedTotalTimeErrors;
  }

  public void setExpectedTotalTimeErrors(List<ValidationErrorMessage> expectedTotalTimeErrors) {
    this.expectedTotalTimeErrors = expectedTotalTimeErrors;
  }

  public List<ValidationErrorMessage> getUnknownErrors() {
    return unknownErrors;
  }

  public void setUnknownErrors(List<ValidationErrorMessage> unknownErrors) {
    this.unknownErrors = unknownErrors;
  }
}
