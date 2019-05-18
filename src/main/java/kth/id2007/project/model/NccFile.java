package kth.id2007.project.model;

import org.w3c.dom.Document;

/**
 * @author Kim Hammar on 2019-05-17.
 */
public class NccFile {

  String path;
  String name;
  Document nccDoc;

  public NccFile(String path, String name, Document nccDoc) {
    this.path = path;
    this.name = name;
    this.nccDoc = nccDoc;
  }

  public NccFile() {
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

  public Document getNccDoc() {
    return nccDoc;
  }

  public void setNccDoc(Document nccDoc) {
    this.nccDoc = nccDoc;
  }
}
