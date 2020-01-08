package org.dareon.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JTI {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private long id;

	private String jti;

	public JTI() {
		super();
	}

	public JTI(final String jti) {
		super();
		this.jti = jti;
	}

	//
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJti() {
		return jti;
	}

	public void setJti(String jti) {
		this.jti = jti;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Privilege [name=").append(jti).append("]").append("[id=").append(jti).append("]");
		return builder.toString();
	}
}
