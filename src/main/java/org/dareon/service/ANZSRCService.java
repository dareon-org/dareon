package org.dareon.service;

import java.util.List;

import org.dareon.domain.ANZSRC;
import org.dareon.domain.Repo;
import org.dareon.repository.ANZSRCRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ANZSRCService
{

    private ANZSRCRepository aNZSRCRepository;

    @Autowired
    public ANZSRCService(ANZSRCRepository aNZSRCRepository)
    {
	this.aNZSRCRepository = aNZSRCRepository;
    }

    public ANZSRC get(Long id)
    {
	return aNZSRCRepository.findOne(id);
    }

    public ANZSRC save(ANZSRC aNZSRC)
    {
	return aNZSRCRepository.save(aNZSRC);
	
    }

    public List<ANZSRC> list()
    {
	return aNZSRCRepository.findAllByOrderById();
    }


    public ANZSRC findById(Long id)
    {
	return aNZSRCRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	aNZSRCRepository.delete(id);
	
    }
}