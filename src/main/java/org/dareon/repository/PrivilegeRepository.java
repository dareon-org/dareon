package org.dareon.repository;

import org.dareon.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 * defines Privilege repository extending super class JpaRepository defining
 * privilege
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

	Privilege findByName(String name);

	@Override
	void delete(Privilege privilege);

}
