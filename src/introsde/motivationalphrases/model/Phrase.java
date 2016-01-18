package introsde.motivationalphrases.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


//XmlRootElement defines the root element of the XML tree to which this class will be serialized
@XmlRootElement(name = "phrase")
//XmlAccessorType indicates what to use to create the mapping: either FIELDS, PROPERTIES (i.e., getters/setters), PUBLIC_MEMBER or NONE (which means, you should indicate manually)
@XmlAccessorType(XmlAccessType.FIELD)
public class Phrase {
	@XmlElement
	private String text;
	
	public Phrase() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
