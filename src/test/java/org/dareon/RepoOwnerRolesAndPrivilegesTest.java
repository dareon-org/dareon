package org.dareon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RepoOwnerRolesAndPrivilegesTest {

	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	// REPOSITORY TEST
	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canViewRepoList() throws Exception {

		this.mockMvc.perform(get("/repo/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/list"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canReadAnyRepo() throws Exception {

		this.mockMvc.perform(get("/repo/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/read"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void cannotCreateRepo() throws Exception {
		this.mockMvc.perform(get("/repo/create")).andExpect(status().is4xxClientError());
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canUpdateOwnRepo() throws Exception {
		this.mockMvc.perform(get("/repo/edit/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/create"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void cannotDeleteRepo() throws Exception {
		this.mockMvc.perform(get("/repo/deleteconfirmed/2")).andExpect(status().is4xxClientError());
	}

	// CALLS FOR PROPOSAL TEST
	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canViewCFPList() throws Exception {

		this.mockMvc.perform(get("/callforproposals/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/list"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canReadAnyCFP() throws Exception {

		this.mockMvc.perform(get("/callforproposals/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/read"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canCreateCFP() throws Exception {
		this.mockMvc.perform(get("/callforproposals/create")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/create"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canUpdateOwnCFP() throws Exception {
		this.mockMvc.perform(get("/callforproposals/edit/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/create"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canDeleteCFP() throws Exception {
		this.mockMvc.perform(get("/callforproposals/delete/1")).andExpect(status().is4xxClientError());
	}

	// PROPOSAL PRIVILEGE TEST
	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canViewProposalList() throws Exception {

		this.mockMvc.perform(get("/proposal/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/list"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void canReadAnyproposal() throws Exception {

		this.mockMvc.perform(get("/proposal/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/read"));
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void cannotcreateProposal() throws Exception {
		this.mockMvc.perform(get("/proposal/create")).andExpect(status().is4xxClientError());
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void cannotUpdateproposal() throws Exception {
		this.mockMvc.perform(get("/proposal/edit/2")).andExpect(status().is4xxClientError());
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("repoowner@rmit.edu.au") public void cannotDeleteProposal()
	 * throws Exception { this.mockMvc.perform(get("/proposal/delete/2"))
	 * .andExpect(status().is4xxClientError()); }
	 */

}
