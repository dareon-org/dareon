package org.dareon;

import org.dareon.domain.Repo;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthorizeDeauthorizeROWebLayerTest {

	@Autowired
	CFPService cFPService;

	@Autowired
	RepoService repoService;

	@Autowired
	UserService userService;

	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void saAuthorizeROtoManageRepo() throws Exception {
		long numberOfRepositories = repoService.listForSA().size();

		// Admin edits a repository
		this.mockMvc
				.perform(post("/repo/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("repo.id", "1") // search for repository to edit
						.param("repo.title", "Test Repository 1").param("repo.institution", "Test Institution 1")
						.param("repo.shortDescription", "Test shortDescription 1")
						.param("repo.description", "Test Description 1").param("repo.status", "true")
						.param("repo.creator", "2").param("repo.owner", "3") // Authorize "repoowner@rmit.edu.au" to
																				// manage repo
						.param("repoForm.domains", "{3,4}"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/1"));

		// check if the repository owner was updated
		MvcResult response = mockMvc.perform(get("/repo/read/1")).andExpect(status().isOk())
				.andExpect(view().name("repo/read")).andExpect(model().attributeExists("repo")).andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		Repo rv = (Repo) repoValue;

		// number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositories, repoService.listForSA().size());
		// check if the same repo
		assertEquals("failure - ID attribute not match", rv.getId(), 1);
		// Owner should have changed from admin@dareon.org to repoowner@rmit.edu.au
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "repoowner@rmit.edu.au");
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void saDeAuthorizeROtoManageRepo() throws Exception {
		long numberOfRepositories = repoService.listForSA().size();

		this.mockMvc.perform(post("/repo/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON).param("repo.id", "1") // search for repository to edit
				.param("repo.title", "Test Repository 1").param("repo.institution", "Test Institution 1")
				.param("repo.shortDescription", "Test shortDescription 1")
				.param("repo.description", "Test Description 1").param("repo.status", "true").param("repo.creator", "2") // Systems
																															// Administrator
																															// ID
				.param("repo.owner", "2") // De-authorize repoowner@rmit.edu.au, change owner to admin@dareon.org
				.param("repoForm.domains", "{3,4}")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("read/1"));

		// check if the repository owner was updated
		MvcResult response = mockMvc.perform(get("/repo/read/1")).andExpect(status().isOk())
				.andExpect(view().name("repo/read")).andExpect(model().attributeExists("repo")).andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		Repo rv = (Repo) repoValue;

		// number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositories, repoService.listForSA().size());
		// check if the same repo
		assertEquals("failure - ID attribute not match", rv.getId(), 1);
		// Owner should have changed from repoowner@rmit.edu.au to admin@dareon.org
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "admin@dareon.org");
	}

	// Note: repoowner@rmit.edu.au is only authorized to repository 2
	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void RoCannotEditRepoThatHeIsNotAuthorizedTest() throws Exception {
		this.mockMvc.perform(get("/repo/edit/1")).andExpect(status().is4xxClientError());
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void RoCanEditRepoThatHeIsAuthorizedTest() throws Exception {
		this.mockMvc.perform(get("/repo/edit/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/create"));
	}

}
