package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="settings")
@NamedQueries({
	@NamedQuery(name="Setting.findAll", query="SELECT s FROM Setting s"),
	@NamedQuery(name="Setting.findAllSettingsAndValues", query="SELECT s.setting, s.value FROM Setting s")
})
public class Setting extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SETTINGS_ID_GENERATOR", sequenceName="SETTINGS_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SETTINGS_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	private String description;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String setting;

	private String value;

	public Setting() {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getModified() {
		return this.modified;
	}

	public String getSetting() {
		return this.setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}