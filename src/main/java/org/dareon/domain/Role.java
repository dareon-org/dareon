package org.dareon.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * Class defines roles and authentication of various users associated with the
 * system
 *
 */
@Entity
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
	private Collection<Privilege> privileges;

	/**
	 * Default Constructor
	 */
	public Role() {
		super();
	}
	
	/**
	 * 
	 * @param name
	 *            returns the name of the user to the immediate super class
	 */
	public Role(final String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 * @return the user id to the id variable (Long Type)
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return User name to the name variable (String Type)
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return th eset of user values to the users object
	 */
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/**
	 * 
	 * @return the Collection of privileges to privilege object.
	 */
	public Collection<Privilege> getPrivileges() {
		return privileges;
	}

	/**
	 * 
	 * @param privileges
	 *            define the value of collection of privilages to privileges object
	 */
	public void setPrivileges(Collection<Privilege> privileges) {
		this.privileges = privileges;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + name + "]";
	}

}
