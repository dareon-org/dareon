package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Classification;

/**
 * 
 *defines Classification repository extending super class CrudRepository defining create retrieve update and delete functionality
 */
public interface ClassificationRepository extends CrudRepository<Classification, Long>
{

    Classification findById(Long id);
    Classification findByName(String name);
    List<Classification> findAllByOrderById();
   

}
