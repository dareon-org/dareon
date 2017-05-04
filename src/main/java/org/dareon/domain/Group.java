package org.dareon.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("GROUP")
@Inheritance
public class Group extends ANZSRC
{

    public Group()
    {
	
    }
    
    public Group(String code, String name)
    {
	this.setCode(code);
	this.setName(name);
	// TODO Auto-generated constructor stub
    }

}
