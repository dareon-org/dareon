package org.dareon.controller;

import java.util.List;

import org.dareon.domain.Classification;
import org.dareon.json.JsonClassificationTree;
import org.dareon.service.ClassificationService;
import org.dareon.wrappers.ClassificationForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * This is class of Spring MVC framework (the component of Spring Framework used
 * to implement Web Application). The @Controller annotation indicates that this
 * class serves the role of a controller.The @Controller annotation acts as a
 * stereotype for the annotated class, indicating its role. The dispatcher scans
 * such annotated classes for mapped methods and detects @RequestMapping
 * annotations and fetch the url for the for classification page..
 * 
 */
@Controller
public class ClassificationController {

	private ClassificationService classificationService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	/**
	 * 
	 * @param classificationService
	 *            returns the value of immediate super class object
	 * 
	 */
	@Autowired
	public ClassificationController(ClassificationService classificationService) {
		super();
		this.classificationService = classificationService;
	}

	@PreAuthorize("hasRole('ROLE_SA')")
	@RequestMapping("/classification/create")
	public String forCreate(Model model) throws JSONException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("classificationForm", new ClassificationForm());
		model.addAttribute("classificationTree",
				JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));
		model.addAttribute("pre", new String());
		model.addAttribute("parent", new String());

		return "classification/create";
	}

	/**
	 * 
	 * @param classificationForm
	 *            stores the FOR values typed by the user in create form to the
	 *            immediate super class
	 * @return the user to 'list, url
	 */
	@PreAuthorize("hasRole('ROLE_SA')")
	@RequestMapping(value = "/classification/create", method = RequestMethod.POST)
	public String forSave(@ModelAttribute ClassificationForm classificationForm) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (classificationForm.getParent() == "") {
			//Classification newFOR = classificationForm.getClassification();
			//Classification savedNewFOR = classificationService.save(newFOR);
		} else {
			Long p = Long.valueOf(classificationForm.getParent()).longValue();
			Classification parent = classificationService.findById((long) p);
			System.out.println(parent.getName());
			Classification newFOR = classificationForm.getClassification();
			List<Classification> newChildren = parent.getChildren();
			newChildren.add(newFOR);
			System.out.println(newChildren.size());
			parent.setChildren(newChildren);
			newFOR.setParent(parent);

			//Classification savedNewFOR = classificationService.save(newFOR);
			//Classification savedParent = classificationService.save(parent);
		}

		return "redirect:list";
	}

	/**
	 * 
	 * @param id
	 *            check the authentication mentioned in the immediate super class
	 *            and allows user to proceed further
	 * @param model
	 *            defines the value of edit model to immediate super class
	 * @return the user to create url for making changes
	 */
	@PreAuthorize("hasRole('ROLE_SA')")
	@RequestMapping("/classification/edit/{id}")
	public String forEdit(@PathVariable Long id, Model model) {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ClassificationForm classificationForm = new ClassificationForm();
		classificationForm.setClassification(classificationService.findById(id));

		model.addAttribute("classificationTree",
				JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));

		JSONArray pre = new JSONArray();

		if (classificationService.findById(id).getParent() != null)
			pre.put(classificationService.findById(id).getParent().getId().toString());

		classificationForm.setPre(pre.toString());
		model.addAttribute("classificationForm", classificationForm);

		return "classification/create";
	}

	@PreAuthorize("hasRole('ROLE_SA')")
	@RequestMapping("/classification/list")
	public String forList(Model model) throws JSONException {
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("classificationForm", new ClassificationForm());
		model.addAttribute("classificationTree",
				JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));
		// model.addAttribute("pre", new String());

		return "classification/list";

	}

	@PreAuthorize("hasRole('ROLE_SA')")
	@RequestMapping("/classification/deleteconfirmed/{id}")
	public RedirectView forDeleteConfirmed(@PathVariable Long id, Model model) {
		classificationService.delete(id);
		return new RedirectView("/classification/list");
	}

}