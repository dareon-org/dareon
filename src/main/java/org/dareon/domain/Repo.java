package org.dareon.domain;

import java.util.Collection;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.dareon.json.JsonDateSerializer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * this class defines the Repo table and relation of its various fields with
 * other table and objects
 *
 */
@Entity
@Table(name = "repos")
public class Repo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(nullable = false)
	private String institution;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String shortDescription;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@ManyToOne()
	@JoinColumn(name = "creator_id", nullable = false)
	private User creator;

	@ManyToOne()
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@Type(type = "true_false")
	private Boolean status = true;
	// set of CFPs as new object(As a hashset)

	@OneToMany(mappedBy = "repo", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH })
	private Set<CFP> cFPs = new HashSet<CFP>();

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "repos_proposalreviewers", joinColumns = { @JoinColumn(name = "repo_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Collection<Classification> proposalReviewers;

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(value = FetchMode.SUBSELECT)
	@JoinTable(name = "repos_fors", joinColumns = { @JoinColumn(name = "repo_id") }, inverseJoinColumns = {
			@JoinColumn(name = "for_id") })
	private Collection<Classification> domains;

	public Repo() {
	}

	/**
	 * 
	 * @param title
	 *            returns Repo title to immediate super class
	 * @param institution
	 *            returns institute name (String type) to the immediate super class
	 * @param shortDescription
	 *            returns definition of the Repository (String type) to the
	 *            immediate super class
	 * @param description
	 *            returns description of the Repository (String type) to the
	 *            immediate super class
	 * @param creator
	 *            returns creator of the Repository (String type) to the immediate
	 *            super class
	 * @param owner
	 *            returns owner of the Repository (String type) to the immediate
	 *            super class
	 * @param status
	 *            returns status of the Repository (String type) to the immediate
	 *            super class
	 */
	public Repo(String title, String institution, String shortDescription, String description, User creator, User owner,
			Boolean status) {
		super();
		this.title = title;
		this.institution = institution;
		this.shortDescription = shortDescription;
		this.description = description;
		this.creator = creator;
		this.owner = owner;
		this.status = status;
		// this.cFPs = cFPs;
		// this.domains = domains;
	}

	/**
	 * 
	 * @return the name of Repo Owner to the owner object(User type)
	 */

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return the name of creator of the repository to the creator object(User
	 *         type)
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * 
	 * @return the date of creation of repo to the createdOn variable(Date type)
	 */
	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return associated institution name with repo to the institution
	 *         variable(String type)
	 */
	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Repo(String name) {
		this.setTitle(name);
	}

	/**
	 * 
	 * @return repo id to the id variable(long type)
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return repo title to the variable title (String type)
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return the definiton of repo to the definition variable(String type)
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDesctiption) {
		this.shortDescription = shortDesctiption;
	}

	/**
	 * 
	 * @return the description of Repo to the description variable(String type)
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return the value of cfp to the set of CFPs
	 */
	public Set<CFP> getcFPs() {
		return cFPs;
	}

	public void setcFPs(Set<CFP> cFP) {
		this.cFPs = cFP;
	}

	/**
	 * 
	 * @return the status of Repo to the status variable(Bolean type)
	 */
	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * 
	 * @return the collection for classification to the domain object
	 */
	public Collection<Classification> getDomains() {
		return domains;
	}

	public void setDomains(Collection<Classification> domains) {
		this.domains = domains;
	}

	/**
	 * 
	 * @return the collection for classification to the proposalReviewers object
	 */
	public Collection<Classification> getProposalReviewers() {
		return proposalReviewers;
	}

	public void setProposalReviewers(Collection<Classification> proposalReviewers) {
		this.proposalReviewers = proposalReviewers;
	}

	@Override
	public String toString() {
		return "Repo [id=" + id + ", name=" + title + ", definition=" + shortDescription + ", description="
				+ description + ", user=" + creator + "]";
	}
}
