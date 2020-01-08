package org.dareon.repository;

import org.dareon.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * defines Role repository extending JpaRepository defining user roles
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

	@Override
	void delete(Role role);

}
