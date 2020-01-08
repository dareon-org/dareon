package org.dareon.controller;

import org.dareon.domain.User;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
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

/**
 * Class defining the controller of home page of the complete system.
 *
 */
@Controller
public class HomeController {

	//private RepoService repoService;
	private UserService userService;
	//private CFPService cFPService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 
	 * @param repoService
	 *            details the reposervice layer and its functions
	 * @param userService
	 *            details the authenticated user service defined by service layer
	 * @param cFPService
	 *            details the calls for proposal service declared in CFP service
	 *            layer class
	 */
	@Autowired
	public HomeController(RepoService repoService, UserService userService, CFPService cFPService) {
		super();
		//this.repoService = repoService;
		this.userService = userService;
		//this.cFPService = cFPService;
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	/**
	 * 
	 * @param model
	 *            detaiks the sys admin model for authority
	 * @return redirects system to authenticated admin to the "user/sysadmin" url
	 */
	@PreAuthorize("hasAuthority('SYSADMIN_CREATE_PRIVILEGE')")
	@RequestMapping("/user/sysadmin")
	public String sysAdminCreate(Model model) {
		model.addAttribute("sysAdmin", new User());
		return "user/sysadmin";

	}

	/**
	 * 
	 * @param sysAdmin
	 *            details about the system administrator
	 * @return redirects system to the read url
	 */
	@PreAuthorize("hasAuthority('SYSADMIN_CREATE_PRIVILEGE')")
	@RequestMapping(value = "/user/sysadmin", method = RequestMethod.POST)
	public String sysAdminSave(@ModelAttribute User sysAdmin) {
		User newUser = userService.save(sysAdmin);
		return "redirect:read/" + newUser.getEmail();

	}

	/**
	 * 
	 * @param email
	 *            details stored email for authentication
	 * @param model
	 *            details authority model
	 * @return redirects the system to "user/read" url
	 */
	@PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
	@RequestMapping("/user/read/{email}")
	public String userRead(@PathVariable String email, Model model) {
		model.addAttribute("user", userService.findByEmail(email));
		return "user/read";

	}

}