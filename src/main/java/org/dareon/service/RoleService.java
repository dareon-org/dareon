package org.dareon.service;

import org.dareon.domain.Role;
import org.dareon.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    
    private RoleRepository roleRepository;
    
    @Autowired
    public RoleService(RoleRepository roleRepository)
    {
	this.roleRepository = roleRepository;
    }
    
    public Role findByName(String name)
    {
	return roleRepository.findByName(name);
    }

    

}
