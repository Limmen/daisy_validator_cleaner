package kth.id2007.project.view;

import kth.id2007.project.model.ErrorType;
import kth.id2007.project.model.NccFile;
import kth.id2007.project.model.ParElement;
import kth.id2007.project.model.ParsedErrorsDTO;
import kth.id2007.project.model.SmilFile;
import kth.id2007.project.model.ValidationErrorMessage;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * GUI Controller, all calls from the view to the model goes through here.
 */
public class GUI {

  private GUI gui = this;

  /**
   * Class constructor
   */
  public GUI() {
    new StartFrame(this);
  }

  /**
   * Main method, entry point of the program. Initializes the GUI.
   *
   * @param args
   */
  public static void main(String[] args) {
    new GUI();
  }

  // Action listener for login-button on the startframe.
  class smilFileSelectorButton implements ActionListener {

    private final JTextField smilDirField;
    private final JFileChooser jfc;

    smilFileSelectorButton(JTextField smilDirField) {
      this.smilDirField = smilDirField;
      jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      jfc.setDialogTitle("Select a folder");
      jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int returnValue = jfc.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        smilDirField.setText(jfc.getSelectedFile().getPath());
      }
    }
  }

  // Action listener for login-button on the startframe.
  class ErrorFileSelectorButton implements ActionListener {

    private final JTextField errorFileField;
    private final JFileChooser jfc;

    ErrorFileSelectorButton(JTextField errorFileField) {
      this.errorFileField = errorFileField;
      jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      jfc.setDialogTitle("Select a validation file");
      jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
      jfc.setAcceptAllFileFilterUsed(false);
      FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml");
      jfc.addChoosableFileFilter(filter);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int returnValue = jfc.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        errorFileField.setText(jfc.getSelectedFile().getPath());
      }
    }
  }

  // Action listener for login-button on the startframe.
  class BackupDirSelectorButton implements ActionListener {

    private final JTextField backupDirField;
    private final JFileChooser jfc;

    BackupDirSelectorButton(JTextField backupDirField) {
      this.backupDirField = backupDirField;
      jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      jfc.setDialogTitle("Select a directory to store backup files");
      jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      int returnValue = jfc.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        backupDirField.setText(jfc.getSelectedFile().getPath());
      }
    }
  }

  // Action listener for login-button on the startframe.
  class RunButton implements ActionListener {

    private final JTextField errorFileField;
    private final JTextField smilDirField;
    private final JTextArea logsArea;
    private final StartFrame startFrame;
    private final JTextField backupDirField;
    private final JCheckBox elapsedTimeCheckbox;
    private final JCheckBox totalTimeCheckbox;
    private final JCheckBox uniqueIdCheckbox;

    RunButton(JTextField smilDirField, JTextField errorFileField, JTextArea logsArea, StartFrame startFrame,
              JTextField backupDirField, JCheckBox elapsedTimeCheckbox, JCheckBox totalTimeCheckbox, JCheckBox uniqueIdCheckbox) {
      this.errorFileField = errorFileField;
      this.smilDirField = smilDirField;
      this.logsArea = logsArea;
      this.startFrame = startFrame;
      this.backupDirField = backupDirField;
      this.elapsedTimeCheckbox = elapsedTimeCheckbox;
      this.totalTimeCheckbox = totalTimeCheckbox;
      this.uniqueIdCheckbox = uniqueIdCheckbox;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      if (!validateInputs(errorFileField, smilDirField, backupDirField, elapsedTimeCheckbox, totalTimeCheckbox, uniqueIdCheckbox)) {
        return;
      }
      try {
        File errorFile = new File(errorFileField.getText());
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setSchema(null);
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        DocumentBuilder documentBuilder = null;
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
        ParsedErrorsDTO parsedErrorsDTO = parseValidationErrorFile(errorFile, documentBuilder, logsArea);
        logsArea.append("Successfully parsed error file and found "
            + parsedErrorsDTO.getExpectedTotalElapsedTimeErrors().size() + " errors of type 'EXPECTED_TOTAL_ELAPSED_TIME (smil)' "
            + ", " + parsedErrorsDTO.getExpectedTotalTimeErrors().size() + " errors of type 'EXPECTED_TOTAL_TIME (ncc)'"
            + ", " + parsedErrorsDTO.getIdMustBeUniqueErrors().size() + ", errors of type 'ID_MUST_BE_UNIQUE (smil)'" +
            " and " + parsedErrorsDTO.getUnknownErrors().size() + " unknown errors.\n");
        List<SmilFile> parsedSmilFiles = parseSmilFileDir(smilDirField.getText(), documentBuilder);
        logsArea.append("Successfully parsed " + parsedSmilFiles.size() + " smil files \n");
        List<NccFile> parsedNccFiles = parseNCCFile(smilDirField.getText(), documentBuilder);
        logsArea.append("Successfully parsed " + parsedNccFiles.size() + " ncc files \n");
        String backupDir = makeBackupDir(backupDirField.getText(), FileSystems.getDefault().getPath(smilDirField.getText()).getFileName().toString());
        logsArea.append("Created backup directory: " + backupDir + "\n");
        correctErrors(parsedErrorsDTO, parsedSmilFiles, logsArea, backupDir, parsedNccFiles, elapsedTimeCheckbox, totalTimeCheckbox, uniqueIdCheckbox);
        startFrame.pack();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
  }

  private void correctErrors(ParsedErrorsDTO parsedErrorsDTO, List<SmilFile> parsedSmilFiles,
                             JTextArea logsArea, String backupDir, List<NccFile> parsedNccFiles,
                             JCheckBox elapsedTimeCheckbox, JCheckBox totalTimeCheckbox, JCheckBox uniqueIdCheckbox)
      throws IOException {
    if(elapsedTimeCheckbox.isSelected()){
      logsArea.append("Correcting TOTAL_ELAPSED_TIME_ERRORS.. \n");
      for (ValidationErrorMessage elapsedTimeError : parsedErrorsDTO.getExpectedTotalElapsedTimeErrors()) {
        logsArea.append("correcting TOTAL_ELAPSED_TIME in file: " + elapsedTimeError.getFile() + "\n");
        copyToBackupDir(elapsedTimeError.getFile(), backupDir, logsArea);
        SmilFile smilFile = findSmilFile(parsedSmilFiles, elapsedTimeError.getFile(), logsArea);
        correctTimeElapsedError(elapsedTimeError, smilFile, logsArea);
      }
    }
    if(totalTimeCheckbox.isSelected()){
      logsArea.append("Correcting TOTAL_TIME_ERRORS.. \n");
      for (ValidationErrorMessage totalTimeError : parsedErrorsDTO.getExpectedTotalTimeErrors()) {
        logsArea.append("correcting TOTAL_TIME in file: " + totalTimeError.getFile() + "\n");
        copyToBackupDir(totalTimeError.getFile(), backupDir, logsArea);
        NccFile nccFile = findNccFile(parsedNccFiles, totalTimeError.getFile(), logsArea);
        correctTotalTimeError(totalTimeError, nccFile, logsArea);
      }
    }
    if(uniqueIdCheckbox.isSelected()){
      logsArea.append("Correcting ID_MUST_BE_UNIQUE_ERRORS.. \n");
      for (String fileName : parsedErrorsDTO.getIdMustBeUniqueErrors().keySet()) {
        logsArea.append("correcting ID_MUST_BE_UNIQUE in file: " + fileName + "\n");
        copyToBackupDir(fileName, backupDir, logsArea);
        SmilFile smilFile = findSmilFile(parsedSmilFiles, fileName, logsArea);
        correctIdMustBeUniqueError(smilFile, logsArea);
      }
    }
    logsArea.append("Done! All errors corrected \n");
  }

  private Boolean correctTimeElapsedError(ValidationErrorMessage validationErrorMessage, SmilFile smilFile, JTextArea logsArea) {
    Document smilDoc = smilFile.getSmilDoc();
    NodeList metaFields = smilDoc.getElementsByTagName("meta");
    for (int i = 0; i < metaFields.getLength(); i++) {
      Element metaElement = (Element) metaFields.item(i);
      if (metaElement.getAttribute("name").toLowerCase().contains("ncc:totalelapsedtime")) {
        String totalElapsedTime = extractExpectedElapsedTime(validationErrorMessage.getMsg());
        metaElement.setAttribute("content", totalElapsedTime);
        try {
          writeModifiedDoc(smilDoc, smilFile.getPath());
          logsArea.append("Successfully updated the total elapsed time in smil file: " + smilFile.getPath() + "\n");
          return true;
        } catch (TransformerException e) {
          logsArea.append("there was an error updating the smil file: " + smilFile.getPath() + "\n");
          logsArea.append(e.getMessage() + "\n");
          return false;
        }
      }
    }
    logsArea.append("Error correcting the total elapsed time error, the totalElapsedTime element was not found in smil file: " + smilFile.getPath() + "\n");
    return false;
  }

  private Boolean correctIdMustBeUniqueError(SmilFile smilFile, JTextArea logsArea) {
    Document smilDoc = smilFile.getSmilDoc();
    NodeList parFields = smilDoc.getElementsByTagName("par");
    orderParElementsById(parFields);
    try {
      writeModifiedDoc(smilDoc, smilFile.getPath());
      logsArea.append("Successfully updated the unique id errors in smil file: " + smilFile.getPath() + "\n");
      return true;
    } catch (TransformerException e) {
      logsArea.append("there was an error updating the smil file: " + smilFile.getPath() + "\n");
      logsArea.append(e.getMessage() + "\n");
      return false;
    }
  }

  private void orderParElementsById(NodeList parFields) {
    List<ParElement> parElements = new ArrayList<>();
    Set<Integer> ids = new HashSet<>();
    for (int i = 0; i < parFields.getLength(); i++) {
      Element parElement = (Element) parFields.item(i);
      String idStr = parElement.getAttribute("id");
      int idNum = Integer.parseInt(idStr.substring(idStr.lastIndexOf("_") + 1, idStr.length()));
      parElements.add(new ParElement(parElement, idStr, idNum));
      ids.add(idNum);
    }

    int shift = 0;
    for (Integer id : ids) {
      List<ParElement> matchPars = parElements.stream().filter(mp -> mp.getNumber() == id).collect(Collectors.toList());
      if(matchPars.size() > 1) {
        Collections.sort(matchPars);
        for (int i = 0; i < matchPars.size(); i++) {
          ParElement matchPar = matchPars.get(i);
          Element elem = matchPar.getElement();
          if(shift > 0 || i > 0) {
            elem.setAttribute("id", "par_" + (matchPar.getNumber() + shift + i));
            NodeList textElems = elem.getElementsByTagName("text");
            for (int j = 0; j < textElems.getLength(); j++) {
              Element textElem = (Element) textElems.item(j);
              textElem.setAttribute("id", "txt_" + (matchPar.getNumber() + shift + i));
            }
          }
        }
        shift = shift+matchPars.size()-1;
      } else {
        if(shift > 0) {
          ParElement matchPar = matchPars.get(0);
          Element elem = matchPar.getElement();
          elem.setAttribute("id", "par_" + (matchPar.getNumber() + shift));
          NodeList textElems = elem.getElementsByTagName("text");
          for (int j = 0; j < textElems.getLength(); j++) {
            Element textElem = (Element) textElems.item(j);
            textElem.setAttribute("id", "txt_" + (matchPar.getNumber() + shift));
          }
        }
      }
    }
  }

  private Boolean correctTotalTimeError(ValidationErrorMessage validationErrorMessage, NccFile nccFile, JTextArea logsArea) {
    Document nccDoc = nccFile.getNccDoc();
    NodeList metaFields = nccDoc.getElementsByTagName("meta");
    for (int i = 0; i < metaFields.getLength(); i++) {
      Element metaElement = (Element) metaFields.item(i);
      if (metaElement.getAttribute("name").toLowerCase().contains("ncc:totaltime")) {
        String totalTime = extractTotalTime(validationErrorMessage.getMsg());
        metaElement.setAttribute("content", totalTime);
        try {
          writeModifiedDoc(nccDoc, nccFile.getPath());
          logsArea.append("Successfully updated the total time in ncc file: " + nccFile.getPath() + "\n");
          return true;
        } catch (TransformerException e) {
          logsArea.append("there was an error updating the ncc file: " + nccFile.getPath() + "\n");
          logsArea.append(e.getMessage() + "\n");
          return false;
        }
      }
    }
    logsArea.append("Error correcting the total time error, the totalTime element was not found in ncc file: " + nccFile.getPath() + "\n");
    return false;
  }

  private void writeModifiedDoc(Document doc, String filePath) throws TransformerException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource source = new DOMSource(doc);
    StreamResult result = new StreamResult(new File(filePath));
    transformer.transform(source, result);

  }

  private String extractExpectedElapsedTime(String msg) {
    int cutIndex = msg.indexOf("but") - 1;
    String temp = msg.substring(0, cutIndex);
    temp = temp.replace("expected total elapsed time ", "");
    return temp;
  }

  private String extractTotalTime(String msg) {
    int cutIndex = msg.indexOf("but") - 1;
    String temp = msg.substring(0, cutIndex);
    temp = temp.replace("expected total time ", "");
    return temp;
  }

  private String makeBackupDir(String path, String smildir) throws IOException {
    Date date = new Date();
    long time = date.getTime();
    String backupDir = smildir + "_" + Long.toString(time);
    Path fullPath = FileSystems.getDefault().getPath(path + "/" + backupDir);
    FileUtils.forceMkdir(fullPath.toFile());
    return fullPath.toString();
  }

  private void copyToBackupDir(String filePath, String backupDir, JTextArea logsArea) {
    try {
      Path fullPath = FileSystems.getDefault().getPath(backupDir + "/" + FileSystems.getDefault().getPath(filePath).getFileName().toString());
      FileUtils.copyFile(new File(filePath), fullPath.toFile());
      logsArea.append("backed up file: " + filePath + " to directory: " + backupDir + "\n");
    } catch (Exception e) {
      logsArea.append("there was an error trying to backup file: " + filePath + "\n");
      logsArea.append(e.getMessage() + "\n");
    }
  }

  private SmilFile findSmilFile(List<SmilFile> parsedSmilFiles, String filePath, JTextArea logsArea) {
    for (SmilFile smilFile : parsedSmilFiles) {
      if (smilFile.getPath().equalsIgnoreCase(filePath)) {
        return smilFile;
      }
    }
    String smilFilesStr = parsedSmilFiles.stream().map(sf -> sf.getPath()).collect(Collectors.joining(","));
    logsArea.append("Could not find smil file with the name: " + filePath + " among the parsed smil files: " + smilFilesStr + "\n");
    return null;
  }

  private NccFile findNccFile(List<NccFile> parsedNccFiles, String filePath, JTextArea logsArea) {
    for (NccFile nccFile : parsedNccFiles) {
      if (nccFile.getPath().equalsIgnoreCase(filePath)) {
        return nccFile;
      }
    }
    String nccFilesStr = parsedNccFiles.stream().map(sf -> sf.getPath()).collect(Collectors.joining(","));
    logsArea.append("Could not find ncc file with the name: " + filePath + " among the parsed ncc files: " + nccFilesStr + "\n");
    return null;
  }

  private Boolean validateInputs(JTextField errorFileField, JTextField smilDirField, JTextField backupDirField,
                                 JCheckBox elapsedTimeCheckbox, JCheckBox totalTimeCheckbox, JCheckBox uniqueIdCheckbox) {
    if (smilDirField.getText() == null || smilDirField.getText().isEmpty()) {
      invalidInput(" you must specify a director with smil files ");
      return false;
    }
    if (errorFileField.getText() == null || errorFileField.getText().isEmpty()) {
      invalidInput(" you must specify a valid XML file with errors to correct ");
      return false;
    }
    if (backupDirField.getText() == null || backupDirField.getText().isEmpty()) {
      invalidInput(" you must specify a directory to place backup files ");
      return false;
    }
    if(!elapsedTimeCheckbox.isSelected() && !totalTimeCheckbox.isSelected() && !uniqueIdCheckbox.isSelected()){
      invalidInput(" you must select one or more checkbox with errors to correct ");
      return false;
    }
    return true;
  }

  // Action listener for login-button on the startframe.
  class ClearLogButton implements ActionListener {

    private final JTextArea logsArea;

    ClearLogButton(JTextArea logsArea) {
      this.logsArea = logsArea;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
      logsArea.setText("");
    }
  }

  private List<SmilFile> parseSmilFileDir(String dirName, DocumentBuilder documentBuilder) throws IOException, SAXException {
    List<Path> smilFiles = Files.list(new File(dirName).toPath())
        .filter(path -> path.toString().endsWith(".smil")).collect(Collectors.toList());
    List<SmilFile> parsedSmilFiles = new ArrayList<>();
    for (Path smilFilePath : smilFiles) {
      Document smilFileDoc = documentBuilder.parse(smilFilePath.toFile());
      SmilFile smilFile = new SmilFile();
      smilFile.setPath(smilFilePath.toString());
      smilFile.setName(smilFilePath.getFileName().toString());
      smilFile.setSmilDoc(smilFileDoc);
      parsedSmilFiles.add(smilFile);
    }
    return parsedSmilFiles;
  }

  private List<NccFile> parseNCCFile(String dirName, DocumentBuilder documentBuilder) throws IOException, SAXException {
    List<Path> nccFiles = Files.list(new File(dirName).toPath())
        .filter(path -> path.getFileName().toString().contains("ncc")).collect(Collectors.toList());
    List<NccFile> parsedNccFiles = new ArrayList<>();
    for (Path nccFilePath : nccFiles) {
      Document nccFileDoc = documentBuilder.parse(nccFilePath.toFile());
      NccFile nccFile = new NccFile();
      nccFile.setPath(nccFilePath.toString());
      nccFile.setName(nccFilePath.getFileName().toString());
      nccFile.setNccDoc(nccFileDoc);
      parsedNccFiles.add(nccFile);
    }
    return parsedNccFiles;
  }

  private ParsedErrorsDTO parseValidationErrorFile(
      File validationErrorFile, DocumentBuilder documentBuilder, JTextArea logsArea) throws IOException, SAXException {
    Document validationErrorDoc = documentBuilder.parse(validationErrorFile);
    validationErrorDoc.getDocumentElement().normalize();
    NodeList errorMsgs = validationErrorDoc.getElementsByTagName("message");
    List<ValidationErrorMessage> idMustBeUniqueErrors = new ArrayList<>();
    List<ValidationErrorMessage> expectedTotalElapsedTimeErrors = new ArrayList<>();
    List<ValidationErrorMessage> expectedTotalTimeErrors = new ArrayList<>();
    List<ValidationErrorMessage> unknownErrors = new ArrayList<>();
    for (int i = 0; i < errorMsgs.getLength(); i++) {
      Element errorMsgElement = (Element) errorMsgs.item(i);
      ValidationErrorMessage validationErrorMessage = new ValidationErrorMessage();
      try {
        validationErrorMessage.setMsg(errorMsgElement.getAttribute("msg"));
      } catch (Exception e) {
        logsArea.append("failed to parse the errorMsg for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      try {
        validationErrorMessage.setCol(errorMsgElement.getAttribute("col"));
      } catch (Exception e) {
        logsArea.append("failed to parse the col for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      try {
        validationErrorMessage.setFile(errorMsgElement.getAttribute("file").replace("file:", ""));
      } catch (Exception e) {
        logsArea.append("failed to parse the file for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      try {
        validationErrorMessage.setLevel(errorMsgElement.getAttribute("level"));
      } catch (Exception e) {
        logsArea.append("failed to parse the level for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      try {
        validationErrorMessage.setLine(Integer.parseInt(errorMsgElement.getAttribute("line")));
      } catch (Exception e) {
        logsArea.append("failed to parse the line for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      try {
        validationErrorMessage.setErrorType(inferErrorType(errorMsgElement.getAttribute("msg")));
      } catch (Exception e) {
        logsArea.append("failed to parse the errorType for element: " + errorMsgElement.toString());
        logsArea.append(e.getMessage() + "\n");
      }
      switch (validationErrorMessage.getErrorType()) {
        case UNKNOWN_ERROR:
          unknownErrors.add(validationErrorMessage);
          break;
        case ID_MUST_BE_UNIQUE:
          idMustBeUniqueErrors.add(validationErrorMessage);
          break;
        case EXPECTED_TOTAL_ELAPSED_TIME:
          expectedTotalElapsedTimeErrors.add(validationErrorMessage);
          break;
        case EXPECTED_TOTAL_TIME:
          expectedTotalTimeErrors.add(validationErrorMessage);
          break;
      }
    }
    ParsedErrorsDTO parsedErrorsDTO = new ParsedErrorsDTO();
    parsedErrorsDTO.setExpectedTotalElapsedTimeErrors(expectedTotalElapsedTimeErrors);
    parsedErrorsDTO.setUnknownErrors(unknownErrors);
    parsedErrorsDTO.setExpectedTotalTimeErrors(expectedTotalTimeErrors);
    parsedErrorsDTO.setIdMustBeUniqueErrors(groupIdErrorsByFile(idMustBeUniqueErrors));
    return parsedErrorsDTO;
  }

  private Map<String, List<ValidationErrorMessage>> groupIdErrorsByFile(List<ValidationErrorMessage> validationErrorMessages) {
    Map<String, List<ValidationErrorMessage>> grouping = new HashMap<>();
    for (ValidationErrorMessage validationMsg : validationErrorMessages) {
      if (grouping.containsKey(validationMsg.getFile())) {
        grouping.get(validationMsg.getFile()).add(validationMsg);
      } else {
        List<ValidationErrorMessage> valList = new ArrayList<>();
        valList.add(validationMsg);
        grouping.put(validationMsg.getFile(), valList);
      }
    }
    return grouping;
  }

  private ErrorType inferErrorType(String errorMsg) {
    if (errorMsg.toLowerCase().contains("of type id must be unique")) {
      return ErrorType.ID_MUST_BE_UNIQUE;
    }
    if (errorMsg.toLowerCase().contains("expected total elapsed time")) {
      return ErrorType.EXPECTED_TOTAL_ELAPSED_TIME;
    }
    if (errorMsg.toLowerCase().contains("expected total time")) {
      return ErrorType.EXPECTED_TOTAL_TIME;
    }
    return ErrorType.UNKNOWN_ERROR;
  }

  //invalidInput dialog
  private void invalidInput(String errorStr) {
    SwingUtilities.invokeLater(() ->
        JOptionPane.showMessageDialog(null, "Please fill all fields | " + errorStr,
            "Invalid input", JOptionPane.INFORMATION_MESSAGE)
    );
  }
}
