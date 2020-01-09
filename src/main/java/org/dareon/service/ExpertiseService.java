package org.dareon.service;

import java.util.Collection;
import java.util.List;

import org.dareon.domain.Expertise;
import org.dareon.domain.Classification;
import org.dareon.domain.User;
import org.dareon.repository.ExpertiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpertiseService {

	private ExpertiseRepository expertiseRepository;
	private ClassificationService classificationService;

	@Autowired
	public ExpertiseService(ExpertiseRepository ExpertiseRepository, ClassificationService classificationService) {
		this.expertiseRepository = ExpertiseRepository;
		this.classificationService = classificationService;
	}

	public Expertise get(Long id) {
		return expertiseRepository.findOne(id);
	}

	public Expertise save(Expertise Expertise) {

		return expertiseRepository.save(Expertise);

	}

	public void setValues(List<Expertise> expertises) {

		for (Expertise e : expertises) {
			expertiseRepository.delete(expertiseRepository.findByClassification(e.getClassification()));
			expertiseRepository.save(e);
		}

	}

	public void saveAll(User user, Collection<Long> ids) {
		List<Expertise> old = expertiseRepository.findAllByUser(user);
		for (Expertise e : old) {
			if (!ids.contains(e.getClassification().getId()))
				expertiseRepository.delete(e);
			else
				ids.remove(e.getClassification().getId());
		}
		for (Long id : ids) {
			expertiseRepository.save(new Expertise(user, classificationService.findById(id), 0));
		}

	}

	public Expertise findById(Long id) {
		return expertiseRepository.findById(id);
	}

	public Expertise findByFOR(Classification classification) {
		return expertiseRepository.findByClassification(classification);
	}

	public List<Expertise> findByUser(User user) {
		return expertiseRepository.findByUser(user);
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		expertiseRepository.delete(id);

	}

	public void delete(Expertise expertise) {
		// TODO Auto-generated method stub
		expertiseRepository.delete(expertise);

	}
}
