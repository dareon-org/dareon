package org.dareon.service;

import java.util.ArrayList;
import java.util.List;

import org.dareon.domain.Classification;
import org.dareon.repository.ClassificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassificationService {

	private ClassificationRepository classificationRepository;

	@Autowired
	public ClassificationService(ClassificationRepository classificationRepository) {
		this.classificationRepository = classificationRepository;
	}

	public Classification get(Long id) {
		return classificationRepository.findOne(id);
	}

	public Classification save(Classification classification) {

		return classificationRepository.save(classification);

	}

	public List<Classification> list() {
		List<Classification> data = classificationRepository.findAllByOrderById();
		List<Classification> factoredData = new ArrayList<Classification>();
		for (Classification e : data) {
			if (e.getParent() == null) {
				factoredData.add(e);
			}
		}
		return factoredData;

	}

	public Classification findById(Long id) {
		return classificationRepository.findById(id);
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		classificationRepository.delete(id);

	}
}
