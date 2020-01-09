package org.dareon.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.dareon.domain.Repo;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

	private UserRepository userRepository;
	private RepoRepository repoRepository;

	@Autowired
	public RepoService(RepoRepository repoRepository, UserRepository userRepository) {
		this.repoRepository = repoRepository;
		this.userRepository = userRepository;
	}

	public Repo get(Long id) {
		return repoRepository.findOne(id);
	}

	public Repo save(Repo repo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Repo tempRepo = repoRepository.findById(repo.getId());
		if (tempRepo == null) {
			repo.setCreator(userRepository.findByEmail(auth.getName()));
			return repoRepository.save(repo);
		} else {
			Repo editRepo = new Repo();
			editRepo.setId(repo.getId());
			editRepo.setcFPs(repo.getcFPs());
			editRepo.setCreatedOn(tempRepo.getCreatedOn());
			editRepo.setCreator(tempRepo.getCreator());
			editRepo.setDomains(repo.getDomains());
			editRepo.setDescription(repo.getDescription());
			editRepo.setShortDescription(repo.getShortDescription());
			editRepo.setInstitution(repo.getInstitution());
			editRepo.setOwner(repo.getOwner());
			editRepo.setProposalReviewers(repo.getProposalReviewers());
			editRepo.setStatus(repo.getStatus());
			editRepo.setTitle(repo.getTitle());

			return repoRepository.save(editRepo);
		}
	}

	public void delete(Repo repo) {
		repoRepository.delete(repo.getId());
	}

	public void delete(Long id) {
		System.out.println(id);
		repoRepository.delete(id);
	}

	@Transactional()
	public List<Repo> listForSA() {
		return repoRepository.findAllByOrderByCreatedOnDesc();
	}

	@Transactional()
	public List<Repo> listForOtherRoles() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Repo> repos = new ArrayList<Repo>();
		repos.addAll(repoRepository.findAllByStatusOrderByCreatedOnDesc(true));
		repos.addAll(repoRepository
				.findAllByOwnerAndStatusOrderByCreatedOnDesc(userRepository.findByEmail(auth.getName()), false));
		return repos;
	}

	public Repo findByTitle(String title) {
		return repoRepository.findByTitle(title);
	}

	public Repo findById(Long Id) {
		return repoRepository.findById(Id);
	}

}
