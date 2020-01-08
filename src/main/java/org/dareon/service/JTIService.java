package org.dareon.service;

import org.dareon.domain.JTI;
import org.dareon.repository.JTIRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JTIService
{
    
    private JTIRepository jTIRepository;
    
    @Autowired
    public JTIService(JTIRepository jTIRepository)
    {
	this.jTIRepository = jTIRepository;
    }
    
    public JTI save(String jti)
    {
	return jTIRepository.save(new JTI(jti));
    }
    
    public boolean exists(String jti)
    {
	if(jTIRepository.findByJti(jti) == null)
	    return false;
	return true;
    }

}
