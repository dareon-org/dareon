package org.dareon.wrappers;

import java.util.ArrayList;
import java.util.List;
import org.dareon.domain.Expertise;
import org.dareon.domain.User;

public class AddExpertiseValueForm {
	private User user;
	private List<Expertise> expertises;
	private String pre;

	public AddExpertiseValueForm() {
		super();
		expertises = new ArrayList<Expertise>();
		// TODO Auto-generated constructor stub
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Expertise> getExpertises() {
		return expertises;
	}

	public void setExpertises(List<Expertise> expertises) {
		this.expertises = expertises;
	}

	public static boolean isNumeric(String str) {
		try {
			Long.parseLong(str);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}

	}
}
