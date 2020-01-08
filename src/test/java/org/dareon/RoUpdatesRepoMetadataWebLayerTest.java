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
public class RoUpdatesRepoMetadataWebLayerTest {

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
	@WithUserDetails("repoowner@rmit.edu.au")
	public void roCanUpdateOwnedRepoMetadataTest() throws Exception {
		long numberOfRepositories = repoService.listForSA().size();

		this.mockMvc
				.perform(post("/repo/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("repo.id", "2") // search for repository to edit
						.param("repo.title", "Test Repository 2").param("repo.institution", "Test Institution 2")
						.param("repo.shortDescription", "Modified shortDescription") // modified contents
						.param("repo.description", "Test Description 2").param("repo.status", "true")
						.param("repo.creator", "2").param("repo.owner", "3").param("repoForm.domains", "{3,4}"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/2"));

		// check if the repository was updated
		MvcResult response = mockMvc.perform(get("/repo/read/2")).andExpect(status().isOk())
				.andExpect(view().name("repo/read")).andExpect(model().attributeExists("repo")).andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		Repo rv = (Repo) repoValue;

		assertNotNull("failure - not null", repoValue);
		// number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositories, repoService.listForSA().size());
		assertEquals("failure - ID attribute not match", rv.getId(), 2);
		assertEquals("failure - Repository attribute not match", rv.getTitle(), "Test Repository 2");
		// the shortDescription should have changed
		assertEquals("failure - shortDescription attribute not match", rv.getShortDescription(),
				"Modified shortDescription");
		assertEquals("failure - Description attribute not match", rv.getDescription(), "Test Description 2");
		assertEquals("failure - Institution attribute not match", rv.getInstitution(), "Test Institution 2");
		assertEquals("failure - Status attribute not match", rv.getStatus(), true);
		assertEquals("failure - Creator attribute not match", rv.getCreator().getEmail(), "admin@dareon.org");
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "repoowner@rmit.edu.au");
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void roCannotUpdateRepoHeDoesntOwnMetadataTest() throws Exception {

		this.mockMvc
				.perform(post("/repo/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("repo.id", "1") // search for repository to edit
						.param("repo.title", "Test Repository 1").param("repo.institution", "Test Institution 1")
						.param("repo.shortDescription", "Modified shortDescription") // modified contents
						.param("repo.description", "Test Description 1").param("repo.status", "true")
						.param("repo.creator", "2").param("repo.owner", "2").param("repoForm.domains", "{3,4}"))
				.andExpect(status().is4xxClientError());

	}

}
