package org.dareon.controller;

import java.util.Arrays;
import org.dareon.domain.Proposal;
import org.dareon.service.CFPService;
import org.dareon.service.ProposalService;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
 * controller class Proposal field and functions in MVC structure implementing
 * request mapping
 * 
 */
@Controller
public class ProposalController {

	private CFPService cFPService;
	// private UserService userService;
	private ProposalService proposalService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 
	 * @param cFPService
	 *            returns details about the call for proposal service layer class to
	 *            the immediate super class
	 * @param userService
	 *            returns details about the authenticated user defined by user
	 *            service layer class to the immediate super class
	 * @param proposalService
	 *            returns details about the proposal service layer class to the
	 *            immediate super class
	 * 
	 */
	@Autowired
	public ProposalController(CFPService cFPService, UserService userService, ProposalService proposalService) {
		super();
		this.cFPService = cFPService;
		// this.userService = userService;
		this.proposalService = proposalService;
	}

	/**
	 * 
	 * @param model
	 *            details authority model for create functionality
	 * @return redirects the system to "proposal/create" url
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
	@RequestMapping("/proposal/create")
	public String proposalCreate(Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("cfps", cFPService.list());
		model.addAttribute("proposal", new Proposal());
		return "proposal/create";
	}

	/**
	 * 
	 * @param model
	 *            details authority model for create functionality
	 * @param id
	 *            details user id for create functionality for authenticated user
	 * @return redirects system to "proposal/create" url
	 */

	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
	@RequestMapping("/proposal/create/{id}")
	public String proposalCreateWithPreselectedRepo(Model model, @PathVariable Long id) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("repos", Arrays.asList(cFPService.findById(id)));
		model.addAttribute("proposal", new Proposal());
		return "proposal/create";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
	@RequestMapping(value = "/proposal/create", method = RequestMethod.POST)
	public String proposalSave(@ModelAttribute Proposal proposal) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Proposal savedproposal = proposalService.save(proposal);

		return "redirect:read/" + savedproposal.getId();
	}

	/**
	 * 
	 * 
	 * 
	 * @param model
	 *            details authority model for edit functionality
	 * @param id
	 *            details user id for edit functionality for authenticated user
	 * @return redirects system to "proposal/create" url ..
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_EDIT_PRIVILEGE'))")
	@RequestMapping("/proposal/edit/{id}")
	public String proposalEdit(@PathVariable Long id, Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("cfps", Arrays.asList(proposalService.findById(id).getCfp()));
		model.addAttribute("proposal", proposalService.findById(id));
		return "proposal/create";
	}

	/**
	 * 
	 * @param model
	 *            details authority model for read functionality
	 * @param id
	 *            details user id for read functionality for authenticated user
	 * @return redirects system to "proposal/read" url
	 */
	@PreAuthorize("hasAuthority('PROPOSAL_READ_PRIVILEGE')")
	@RequestMapping("/proposal/read/{id}")
	public String proposalRead(@PathVariable Long id, Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("proposal", proposalService.findById(id));
		return "proposal/read";
	}

	/**
	 * 
	 * @param model
	 *            details authority model for delete functionality
	 * @param id
	 *            details user id for delete functionality for authenticated user
	 * @return redirects system to "proposal/delete" url after checking the
	 *         authority
	 */
	@PreAuthorize("hasAuthority('PROPOSAL_READ_PRIVILEGE')")
	@RequestMapping("/proposal/list")
	public String proposalList(Model model) {
		model.addAttribute("proposals", proposalService.list());
		return "proposal/list";
	}

	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_DELETE_PRIVILEGE'))")
	@RequestMapping("/proposal/delete/{id}")
	public String proposalDelete(@PathVariable Long id, Model model) {
		model.addAttribute("proposal", proposalService.findById(id));
		return "proposal/delete";
	}

	/**
	 * 
	 * @param model
	 *            details authority model for delete functionality
	 * @param id
	 *            details user id for delete functionality for authenticated user
	 * @return redirects system to "proposal/create" url after user deletes the
	 *         proposal
	 */
	@PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_DELETE_PRIVILEGE'))")
	@RequestMapping("/proposal/deleteconfirmed/{id}")
	public RedirectView proposalDeleteConfirmed(@PathVariable Long id, Model model) {
		System.out.println(id);
		proposalService.delete(id);
		return new RedirectView("/proposal/list");
	}

}