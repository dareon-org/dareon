package org.dareon.controller;

import java.util.Arrays;
import org.dareon.domain.CFP;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * This class implements the main functionality of the different services
 * provided by our system and defines its relation with service layer
 */

@Controller
public class CFPController {

	private RepoService repoService;
	private UserService userService;
	private CFPService cFPService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 
	 * @param repoService
	 *            defines repository services and returns the value to immediate
	 *            super class
	 * @param userService
	 *            define different user services and returns the value to immediate
	 *            super class
	 * @param cFPService
	 *            defines various call for proposal services and returns the value
	 *            to immediate super class
	 */
	@Autowired
	public CFPController(RepoService repoService, UserService userService, CFPService cFPService) {
		super();
		this.repoService = repoService;
		this.userService = userService;
		this.cFPService = cFPService;
	}

	/**
	 * 
	 * @param model
	 *            defines the main structure of CFP create function
	 * @return the value of callforproposals/create to the object CFP
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('CFP_CREATE_PRIVILEGE')")
	@RequestMapping("/callforproposals/create")
	public String cFPCreate(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("repos", userService.findByEmail(auth.getName()).getOwnedRepos());
		model.addAttribute("callForProposals", new CFP());
		return "callforproposals/create";
	}

	/**
	 * 
	 * @param model
	 *            defines the main structure of the CFP create function with pre
	 *            selected repository and the path variable.
	 * @param id
	 *            defines the Repo owner id of the selected repo for which it is
	 *            authenticated to create
	 * @return the callforproposals/create model to the CFP object
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('CFP_CREATE_PRIVILEGE') AND isRepoOwner(#id))")
	@RequestMapping("/callforproposals/create/{id}")
	public String cFPCreateWithPreselectedRepo(Model model, @PathVariable Long id) {
		model.addAttribute("repos", Arrays.asList(repoService.findById(id)));
		model.addAttribute("callForProposals", new CFP());
		return "callforproposals/create";
	}

	/**
	 * 
	 * @param cFP
	 *            defines the th esave functionfor saving newly created cfp in cFP
	 *            object
	 * @return the savedCallForProposal to CFP object
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('CFP_CREATE_PRIVILEGE') AND isRepoOwner(#cFP))")
	@RequestMapping(value = "/callforproposals/create", method = RequestMethod.POST)
	public String cFPSave(@ModelAttribute CFP cFP) {
		// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
		// if (callForProposals.getUser() == null)
		// callForProposals.setUser(repoService.findByTitle(title)));
		// return callForProposals.getRepo().toString();
		CFP savedCallForProposals = cFPService.save(cFP);

		return "redirect:read/" + savedCallForProposals.getId();
	}

	/**
	 * 
	 * @param id
	 *            defines the CFP owner id
	 * @param model
	 *            defines the edit function link with controller
	 * @return value "callforproposals/create"
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('CFP_EDIT_PRIVILEGE') AND isRepoAndCFPOwner(#id))")
	@RequestMapping("/callforproposals/edit/{id}")
	public String cFPEdit(@PathVariable Long id, Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("repos", Arrays.asList((cFPService.findById(id)).getRepo()));
		model.addAttribute("callForProposals", cFPService.findById(id));
		return "callforproposals/create";
	}

	/**
	 * 
	 * @param id
	 *            defines the CFP owner id
	 * @param model
	 *            the read function and its link with controller
	 * @return value "callforproposals/read"
	 */
	@PreAuthorize("hasAuthority('CFP_READ_PRIVILEGE')")
	@RequestMapping("/callforproposals/read/{id}")
	public String cFPRead(@PathVariable Long id, Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("callForProposals", cFPService.findById(id));
		return "callforproposals/read";
	}

	/**
	 * 
	 * @param model
	 *            defines the read function and its link with controller
	 * @return value "callforproposals/list" to redirect view to the list page
	 */
	@PreAuthorize("hasAuthority('CFP_READ_PRIVILEGE')")
	@RequestMapping("/callforproposals/list")
	public String cFPList(Model model) {
		model.addAttribute("callsForProposals", cFPService.list());
		return "callforproposals/list";
	}

	/**
	 * 
	 * @param id
	 *            defines the CFP owner id
	 * @param model
	 *            defines the delete function and its link with controller
	 * @return redirects the page to "callforproposals/delete"
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('CFP_DELETE_PRIVILEGE') AND isRepoAndCFPOwner(#id))")
	@RequestMapping("/callforproposals/delete/{id}")
	public String cFPDelete(@PathVariable Long id, Model model) {
		model.addAttribute("callForProposals", cFPService.findById(id));
		return "callforproposals/delete";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('CFP_DELETE_PRIVILEGE') AND isRepoAndCFPOwner(#id))")
	@RequestMapping("/callforproposals/deleteconfirmed/{id}")
	public RedirectView cFPDeleteConfirmed(@PathVariable Long id, Model model) {
		System.out.println(id);
		cFPService.delete(id);
		return new RedirectView("/callforproposals/list");
	}

}