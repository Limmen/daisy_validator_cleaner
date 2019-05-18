package kth.id2007.project.model;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Kim Hammar on 2019-05-18.
 */
public class ParElement implements Comparable {

  private Element element;
  private String id;
  private int number;

  public ParElement(Element element, String id, int number) {
    this.element = element;
    this.id = id;
    this.number = number;
  }

  public ParElement() {
  }

  public Element getElement() {
    return element;
  }

  public void setElement(Element element) {
    this.element = element;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }


  @Override
  public int compareTo(Object o) {
    ParElement comparison = (ParElement) o;
    if(comparison.getNumber() > number)
      return -1;
    if (comparison.getNumber() < number)
      return 1;
    NodeList seqElementsComparison = comparison.getElement().getElementsByTagName("seq");
    NodeList seqElementsOwn = element.getElementsByTagName("seq");
    float comparisonMinBegin = Float.MAX_VALUE;
    float ownMinBegin = Float.MAX_VALUE;
    for (int i = 0; i < seqElementsComparison.getLength(); i++) {
      Element seqElement = (Element) seqElementsComparison.item(i);
      NodeList audioElements = seqElement.getElementsByTagName("audio");
      for (int j = 0; j < audioElements.getLength(); j++) {
        Element audioElement = (Element) audioElements.item(j);
        String clipBeginStr = audioElement.getAttribute("clip-begin");
        float clipBeginNum = Float.parseFloat(clipBeginStr.replace("npt=", "").replace("s", ""));
        if(clipBeginNum < comparisonMinBegin){
          comparisonMinBegin = clipBeginNum;
        }
      }
    }
    for (int i = 0; i < seqElementsOwn.getLength(); i++) {
      Element seqElement = (Element) seqElementsOwn.item(i);
      NodeList audioElements = seqElement.getElementsByTagName("audio");
      for (int j = 0; j < audioElements.getLength(); j++) {
        Element audioElement = (Element) audioElements.item(j);
        String clipBeginStr = audioElement.getAttribute("clip-begin");
        float clipBeginNum = Float.parseFloat(clipBeginStr.replace("npt=", "").replace("s", ""));
        if(clipBeginNum < ownMinBegin){
          ownMinBegin = clipBeginNum;
        }
      }
    }
    if(comparisonMinBegin > ownMinBegin)
      return -1;
    if(comparisonMinBegin < ownMinBegin)
      return 1;
    return 0;
  }
}
