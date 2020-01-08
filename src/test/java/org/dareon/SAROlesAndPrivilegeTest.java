package org.dareon;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SAROlesAndPrivilegeTest {
	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	// REPOSITORY TEST
	@Test
	@WithUserDetails("admin@dareon.org")
	public void canViewRepoList() throws Exception {

		this.mockMvc.perform(get("/repo/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/list"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canReadAnyRepo() throws Exception {

		this.mockMvc.perform(get("/repo/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/read"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canCreateRepo() throws Exception {
		this.mockMvc.perform(get("/repo/create")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/create"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canUpdateAnyRepo() throws Exception {
		this.mockMvc.perform(get("/repo/edit/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/create"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canDeleteRepo() throws Exception {
		this.mockMvc.perform(get("/repo/delete/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("repo/delete"));
	}

	// CALLS FOR PROPOSAL TEST
	@Test
	@WithUserDetails("admin@dareon.org")
	public void canViewCFPList() throws Exception {

		this.mockMvc.perform(get("/callforproposals/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/list"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canReadAnyCFP() throws Exception {

		this.mockMvc.perform(get("/callforproposals/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/read"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canCreateCFP() throws Exception {
		this.mockMvc.perform(get("/callforproposals/create")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/create"));
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("admin@dareon.org") public void canUpdateCFP() throws
	 * Exception { this.mockMvc.perform(get("/callforproposals/edit/2"))
	 * .andExpect(status().is2xxSuccessful())
	 * .andExpect(view().name("callforproposals/read/2")); }
	 */

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canDeleteCFP() throws Exception {
		this.mockMvc.perform(get("/callforproposals/delete/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("callforproposals/delete"));
	}

	// PROPOSAL PRIVILEGE TEST
	@Test
	@WithUserDetails("admin@dareon.org")
	public void canViewProposalList() throws Exception {

		this.mockMvc.perform(get("/proposal/list")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/list"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canReadAnyproposal() throws Exception {

		this.mockMvc.perform(get("/proposal/read/1")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/read"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void cancreateProposal() throws Exception {
		this.mockMvc.perform(get("/proposal/create")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/create"));
	}

	@Test
	@WithUserDetails("admin@dareon.org")
	public void canUpdateproposal() throws Exception {
		this.mockMvc.perform(get("/proposal/edit/2")).andExpect(status().is2xxSuccessful())
				.andExpect(view().name("proposal/create"));
	}

	/*
	 * @Test
	 * 
	 * @WithUserDetails("admin@dareon.org") public void canDeleteProposal() throws
	 * Exception { this.mockMvc.perform(get("/proposal/delete/2"))
	 * .andExpect(status().is2xxSuccessful())
	 * .andExpect(view().name("proposal/read")); }
	 */

}
