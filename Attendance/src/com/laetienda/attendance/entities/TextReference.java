package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.laetienda.attendance.utilities.Logger;

@Entity
@Table(name="text_references")
@NamedQuery(name="TextReference.findAll", query="SELECT t FROM TextReference t")
public class TextReference extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TEXT_REFERENCES_ID_GENERATOR", sequenceName="TEXT_REFERENCE_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXT_REFERENCES_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	//bi-directional many-to-one association to Text
	@OneToMany(mappedBy="textReference", cascade = CascadeType.PERSIST, orphanRemoval=true)
	private List<Text> texts;

	public TextReference() {
		super();
	}
	
	public TextReference(Logger log){
		super(log);
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return this.created;
	}

	public Date getModified() {
		return this.modified;
	}

	public List<Text> getTexts() {
		return this.texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}

	public Text addText(Text text) {
		getTexts().add(text);
		text.setTextReference(this);

		return text;
	}

	public Text removeText(Text text) {
		getTexts().remove(text);
		text.setTextReference(null);

		return text;
	}
	
	public void setText(String text){
		/*
		List<Text> oldLines;
		Text oldLine;
		
		if(this.texts != null){
			oldLines = getTexts();
			
			if(oldLines.size() > 0){
				for(int c=0; c < oldLines.size(); c++ ){
					oldLine = oldLines.get(c);
					removeText(oldLine);
				}
			}
		}
		*/
		this.texts = new ArrayList<Text>();
		List<String> lines = formatTextInList(text);
		
		for(String line : lines){
			Text textEntity = new Text(log);
			textEntity.setText(line);
			textEntity.setTextReference(this);
			
			if(this.texts == null){
				this.texts = new ArrayList<Text>();
			}
			
			getTexts().add(textEntity);
		}
	}
	
	public String getText(){
		
		String text = "";
		
		if(getTexts() != null && getTexts().size() > 0){
			List<Text> lines = getTexts();
			
			
			for(Text line : lines){
				text += line.getText();
			}
		}
		
		return text;
	}
	
	private List<String> formatTextInList(String text){
		
		int size = 254;
		List<String> textInList = new ArrayList<String>();
		
		for (int start = 0; start < text.length(); start += size) {
			textInList.add(text.substring(start, Math.min(text.length(), start + size)));
	    }
		
		return textInList;
	}

}