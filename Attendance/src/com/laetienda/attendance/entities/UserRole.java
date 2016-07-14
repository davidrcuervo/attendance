package com.laetienda.attendance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="user_roles")
@NamedQuery(name="UserRole.findAll", query="SELECT u FROM UserRole u")
public class UserRole extends Father implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_ROLES_ID_GENERATOR", sequenceName="USER_ROLES_ID_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ROLES_ID_GENERATOR")
	@Column(name="id", updatable=false)
	private Integer id;

	@Column(name="\"created\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Column(name="\"modified\"", insertable = false, updatable = false, nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modified;

	private String username;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="role")
	private Role roleBean;

	//bi-directional many-to-one association to User
	@ManyToOne
	private User user;

	public UserRole() {
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

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRoleBean() {
		return this.roleBean;
	}

	public void setRoleBean(Role roleBean) {
		this.roleBean = roleBean;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}