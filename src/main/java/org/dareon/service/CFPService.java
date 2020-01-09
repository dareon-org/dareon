package org.dareon.service;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.repository.CFPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * 
 * 
 */
@Service
public class CFPService {

	private CFPRepository cFPRepository;

	@Autowired
	public CFPService(CFPRepository cFPRepository) {
		this.cFPRepository = cFPRepository;
	}

	public CFP get(Long id) {
		return cFPRepository.findOne(id);
	}

	public CFP save(CFP proposal) {
		CFP tempCFP = cFPRepository.findById(proposal.getId());
		if (tempCFP == null)
			return cFPRepository.save(proposal);
		else {
			CFP editCFP = new CFP();
			editCFP.setId(proposal.getId());
			editCFP.setDescription(proposal.getDescription());
			editCFP.setDetails(proposal.getDetails());
			editCFP.setRepo(tempCFP.getRepo());
			editCFP.setCreatedOn(tempCFP.getCreatedOn());
			editCFP.setTitle(proposal.getTitle());
			return cFPRepository.save(editCFP);
		}
	}

	/**
	 * 
	 * @return returns list of CFP repo
	 */
	public List<CFP> list() {
		return cFPRepository.findAllByOrderByCreatedOnDesc();
	}

	/**
	 * 
	 * @param title
	 *            finds CFP repo by title
	 * @return cfp repo after searching by title
	 */
	public CFP findByTitle(String title) {
		return cFPRepository.findByTitle(title);
	}

	/**
	 * 
	 * @param id
	 *            details the CFP Id
	 * @return the CFP repo thus searched by id
	 */
	public CFP findById(Long id) {
		// TODO Auto-generated method stub
		return cFPRepository.findById(id);
	}

	public void delete(Long id) {
		// TODO Auto-generated method stub
		cFPRepository.delete(id);

	}
}
