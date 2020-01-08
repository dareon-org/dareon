package org.dareon.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.dareon.domain.Classification;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.json.JsonClassificationTree;
import org.dareon.service.CFPService;
import org.dareon.service.ClassificationService;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
import org.dareon.wrappers.RepoForm;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * controller class for repository management field and functions in MVC
 * structure implementing request mapping.
 */
@Controller
public class RepoController {

	private RepoService repoService;
	private UserService userService;
	//private CFPService cFPService;
	private ClassificationService classificationService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 
	 * @param repoService
	 * 
	 * 
	 * @param cFPService
	 *            returns details about the call for proposal service layer class to
	 *            the immediate super class
	 * @param userService
	 *            returns details about role of the authenticated user defined by
	 *            user service layer class to the immediate super class
	 * @param repoService
	 *            returns details about role of the the repo service layer class to
	 *            the immediate super class
	 * @param classificationService
	 *            details about role of the Classification service layer class to
	 *            the immediate super class
	 */
	@Autowired
	public RepoController(RepoService repoService, UserService userService, CFPService cFPService,
			ClassificationService classificationService) {
		super();
		this.repoService = repoService;
		this.userService = userService;
		//this.cFPService = cFPService;
		this.classificationService = classificationService;
	}

	/**
	 * 
	 * @param model
	 *            details create privilege model for the user
	 * @return redirects user to the "repo/create" url if has authentication
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('REPO_CREATE_PRIVILEGE')")
	@RequestMapping("/repo/create")
	public String repoCreate(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("repoForm", new RepoForm());
		List<User> users = userService.list();
		users.remove((userService.findByEmail(auth.getName())));
		users.add(0, userService.findByEmail(auth.getName()));
		model.addAttribute("users", users);

		model.addAttribute("classificationTree",
				JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));
		model.addAttribute("domains", new String());
		model.addAttribute("pre", new String());
		return "repo/create";
	}

	/**
	 * 
	 * @param repoForm
	 *            details the fields of repoForm edited by user
	 * @param domains
	 *            details repo domain class attribute(String)
	 * @return redirects user to read url
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('REPO_EDIT_PRIVILEGE') AND isRepoOwner(#repoForm))")
	@RequestMapping(value = "/repo/create", method = RequestMethod.POST)
	public String repoSave(@ModelAttribute RepoForm repoForm, @ModelAttribute String domains) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Repo repo = repoForm.getRepo();

		// repo.setDomains(repoForm.getFORCollection());
		Collection<Classification> classifications = new ArrayList<Classification>();
		for (Long id : repoForm.getFORCollection()) {
			classifications.add(classificationService.findById(id));
		}
		repo.setDomains(classifications);

		Repo savedRepo = repoService.save(repo);

		Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),
				auth.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);

		System.out.println("Domains: " + repoForm.getDomains());
		return "redirect:read/" + savedRepo.getId();
	}

	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('REPO_EDIT_PRIVILEGE') AND isRepoOwner(#id))")
	@RequestMapping("/repo/edit/{id}")
	public String repoEdit(@PathVariable Long id, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("repo", new Repo());
		List<User> users = userService.list();
		users.remove((userService.findByEmail(auth.getName())));
		RepoForm repoForm = new RepoForm();
		repoForm.setRepo(repoService.findById(id));

		users.add(0, userService.findByEmail(auth.getName()));
		model.addAttribute("users", users);

		model.addAttribute("classificationTree",
				JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));

		JSONArray pre = new JSONArray();
		for (Classification a : repoService.findById(id).getDomains()) {
			pre.put(a.getId().toString());
		}
		repoForm.setPre(pre.toString());
		model.addAttribute("repoForm", repoForm);
		System.out.println(pre);
		return "repo/create";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('REPO_READ_PRIVILEGE')")
	@RequestMapping("/repo/read/{id}")
	public String repoRead(@PathVariable Long id, Model model) {
		model.addAttribute("repo", repoService.findById(id));
		return "repo/read";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('REPO_DELETE_PRIVILEGE') AND isRepoOwner(#id))")
	@RequestMapping("/repo/delete/{id}")
	public String repoDelete(@PathVariable Long id, Model model) {
		model.addAttribute("repo", repoService.findById(id));
		return "repo/delete";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('REPO_DELETE_PRIVILEGE') AND isRepoOwner(#id))")
	@RequestMapping("/repo/deleteconfirmed/{id}")
	public RedirectView repoDeleteConfirmed(@PathVariable Long id, Model model) {
		System.out.println(id);
		repoService.delete(id);
		return new RedirectView("/repo/list");
	}

	@PreAuthorize("hasAuthority('REPO_READ_PRIVILEGE')")
	@RequestMapping("/repo/list")
	public String repoList(HttpServletRequest request, Model model) {
		if (request.isUserInRole("ROLE_SA")) {
			model.addAttribute("repos", repoService.listForSA());
		} else {
			model.addAttribute("repos", repoService.listForOtherRoles());
		}

		return "repo/list";
	}
}