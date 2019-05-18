package kth.id2007.project.model;

import org.w3c.dom.Document;

/**
 * @author Kim Hammar on 2019-05-17.
 */
public class SmilFile {

  String path;
  String name;
  Document smilDoc;

  public SmilFile(String path, String name, Document smilDoc) {
    this.path = path;
    this.name = name;
    this.smilDoc = smilDoc;
  }

  public SmilFile() {
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Document getSmilDoc() {
    return smilDoc;
  }

  public void setSmilDoc(Document smilDoc) {
    this.smilDoc = smilDoc;
  }
}
