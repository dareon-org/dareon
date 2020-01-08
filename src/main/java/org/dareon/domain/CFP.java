package org.dareon.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "cfp")

/**
 * 
 * This is domain class for Call For Proposals defining objects used in project
 * database and there relation with different objects
 */
public class CFP {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id

	// defines CFP Id variable of long type generated automatically
	private long id;

	// Defines CFP title variable of string type stored in unique table column with
	// default type
	@Column(nullable = false, unique = true)
	private String title;

	@CreationTimestamp
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String details;

	// joining repo_id for table repo with many to one relationship
	@ManyToOne()
	@JoinColumn(name = "repo_id", nullable = false)
	private Repo repo;

	@OneToMany(mappedBy = "cfp", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH })
	private Set<Proposal> proposals = new HashSet<Proposal>();

	public CFP() {

	}

	/**
	 * 
	 * @param title
	 *            returns CFP title(string type) to the immediate super class
	 * @param description
	 *            returns CFP description(string type) to the immediate super class
	 * @param details
	 *            returns CFP details(string type) to the immediate super class
	 * @param repo
	 *            returns associated CFP repository(repo) to the immediate super
	 *            class
	 */
	public CFP(String title, String description, String details, Repo repo) {
		super();
		this.title = title;
		this.description = description;
		this.details = details;
		this.repo = repo;
	}

	/**
	 * @return value of Id object immediately of long data type
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return value of createdOn object immediately of Date type
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the Title object immediately of String data type
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return the Description object immediately of String data type
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the Details object immediately of String data type
	 */

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @return the Repo object immediately of Repo data type
	 */
	public Repo getRepo() {
		return repo;
	}

	public void setRepo(Repo repo) {
		this.repo = repo;
	}

	/**
	 * 
	 * @return the Proposals in the form of proposal set
	 */
	public Set<Proposal> getProposals() {
		return proposals;
	}

	/**
	 * 
	 * @param proposals
	 *            store the set of proposals in proposal object
	 */

	public void setProposals(Set<Proposal> proposals) {
		this.proposals = proposals;
	}

}
