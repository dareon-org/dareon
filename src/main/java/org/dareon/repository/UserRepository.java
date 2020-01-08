package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.User;

/**
 * 
 * defines User repository interface extending CrudRepository defining create
 * retrieve update and delete functionality
 */
public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);

	List<User> findAllByOrderByIdAsc();
}
