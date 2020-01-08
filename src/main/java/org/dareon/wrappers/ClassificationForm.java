package org.dareon.wrappers;

import org.dareon.domain.Classification;
import org.dareon.service.ClassificationService;

public class ClassificationForm {
	private Classification classification;
	private String pre;
	private String parent;

	public ClassificationForm(ClassificationService classificationService) {
		super();
		classification = new Classification();
		parent = new String();
	}

	public ClassificationForm() {
		super();
		classification = new Classification();
		parent = new String();
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification foR) {
		this.classification = foR;
	}

}
