package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

import com.laetienda.attendance.utilities.Logger;

@Entity
@Table(name="texts")
@NamedQuery(name="Text.findAll", query="SELECT t FROM Text t")
public class Text extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TEXTS_ID_GENERATOR", sequenceName="TEXTS_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TEXTS_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;
	
	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String text;

	//bi-directional many-to-one association to TextReference
	@ManyToOne
	@JoinColumn(name="\"textsID\"")
	private TextReference textReference;

	public Text() {
		super();
	}
	
	public Text(Logger log){
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

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextReference getTextReference() {
		return this.textReference;
	}

	public void setTextReference(TextReference textReference) {
		this.textReference = textReference;
	}
}