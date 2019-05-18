package kth.id2007.project.model;

/**
 * @author Kim Hammar on 2019-05-17.
 */
public class ValidationErrorMessage {

  private String level;
  private String msg;
  private int line;
  private String file;
  private String col;
  private ErrorType errorType;


  public ValidationErrorMessage(String level, String msg, int line, String file, String col, ErrorType errorType) {
    this.level = level;
    this.msg = msg;
    this.line = line;
    this.file = file;
    this.col = col;
    this.errorType = errorType;
  }

  public ValidationErrorMessage() {
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getLine() {
    return line;
  }

  public void setLine(int line) {
    this.line = line;
  }

  public String getFile() {
    return file;
  }

  public void setFile(String file) {
    this.file = file;
  }

  public String getCol() {
    return col;
  }

  public void setCol(String col) {
    this.col = col;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(ErrorType errorType) {
    this.errorType = errorType;
  }

  @Override
  public String toString() {
    return "ValidationErrorMessage{" +
        "level='" + level + '\'' +
        ", msg='" + msg + '\'' +
        ", line=" + line +
        ", file='" + file + '\'' +
        ", col='" + col + '\'' +
        ", errorType=" + errorType +
        '}';
  }
}
