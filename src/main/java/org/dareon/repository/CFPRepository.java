package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CFP;

/**
 * 
 * defines CFP repository extending super class CRUD repository defining create
 * retreive update and delete functionality.
 */
public interface CFPRepository extends CrudRepository<CFP, Long> {

	CFP findById(Long id);

	CFP findByTitle(String title);

	List<CFP> findAllByOrderByCreatedOnDesc();

}
