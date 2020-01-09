package org.dareon.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

/**
 * 
 * Proposals class defines the proposal table and its relation with database
 */

@Entity
@Table(name = "proposals")
public class Proposal {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createdOn;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String details;

	@ManyToOne()
	@JoinColumn(name = "cfp_id", nullable = false)
	private CFP cfp;

	@ManyToOne()
	@JoinColumn(name = "creator_id", nullable = false)
	private User creator;

	public Proposal() {

	}

	/**
	 * 
	 * @param title
	 *            defines the values entered in the title column of proposal table
	 * @param description
	 *            defines the values entered in the description column of proposal
	 *            table
	 * @param details
	 *            defines the values entered in the details column of proposal table
	 * @param cfp
	 *            defines the values(calls for proposals) entered in the CFP column
	 *            of proposal table
	 */

	public Proposal(String title, String description, String details, CFP cfp, User creator) {
		super();
		this.title = title;
		this.description = description;
		this.details = details;
		this.cfp = cfp;
		this.creator = creator;
	}

	/**
	 * 
	 * @return the value of ID to id variable(long type)
	 */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 
	 * @return date of creation to the createdOn variable(Date type)
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * 
	 * @return value of title to the title variable(String type)
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return returns value entered in the descrition section of the table to
	 *         description variable(String type)
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return Details of the proposals entered into details variable(String Type)
	 */
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * 
	 * @return the value of cfp to the cfp object( CFP type)
	 */
	public CFP getCfp() {
		return cfp;
	}

	public void setCfp(CFP cfp) {
		this.cfp = cfp;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

}
