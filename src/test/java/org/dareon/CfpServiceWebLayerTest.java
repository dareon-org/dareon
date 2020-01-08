package org.dareon;

import org.dareon.domain.CFP;
import org.dareon.domain.Proposal;
import org.dareon.service.CFPService;
import org.dareon.service.ProposalService;
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
public class CfpServiceWebLayerTest {

	@Autowired
	ProposalService proposalService;

	@Autowired
	CFPService cfpService;

	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void CfpCreateAndReadWebTest() throws Exception {
		long numberOfCfpPriorToCreate = cfpService.list().size();

		this.mockMvc.perform(post("/callforproposals/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON).param("title", "sample cfp 3")
				.param("institution", "Sample institution for cfp 3").param("details", "Sample details for cfp 3")
				.param("description", "Sample description for cfp 3").param("repo", "2")) // assign to repository: Test
																							// Repo 1
				// check if redirected to CFP read page
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/3")).andReturn();

		long numberOfCfpAfterCreate = cfpService.list().size();

		// check if the CFP was created
		MvcResult response = mockMvc.perform(get("/callforproposals/read/3")).andExpect(status().isOk())
				.andExpect(view().name("callforproposals/read")).andExpect(model().attributeExists("callForProposals"))
				.andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		assertTrue(cfpValue instanceof CFP);
		CFP cv = (CFP) cfpValue;

		assertNotNull("failure - not null", cfpValue);
		// number of CFPs should have increased
		assertEquals("failure - repository did not increased", numberOfCfpPriorToCreate + 1, numberOfCfpAfterCreate);
		assertEquals("failure - ID attribute not match", 3, cv.getId());
		assertEquals("failure - Title attribute not match", "sample cfp 3", cv.getTitle());
		assertEquals("failure - Details attribute not match", "Sample details for cfp 3", cv.getDetails());
		assertEquals("failure - Description attribute not match", "Sample description for cfp 3", cv.getDescription());
		assertEquals("failure - Repository attribute not match", "Test Repo 2", cv.getRepo().getTitle());
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void CfpUpdateAndReadWebTest() throws Exception {

		long numberOfCfpPriorToUpdate = cfpService.list().size();

		this.mockMvc
				.perform(post("/callforproposals/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("id", "1") // search CFP to update
						.param("title", "Test CFP 1").param("institution", "Test CFP Institution 1")
						.param("details", "Modified details") // modified contents
						.param("description", "Test CFP Description 1").param("repo", "2"))
				// check if redirected to CFP read page
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/1"));

		long numberOfCfpAfterUpdate = cfpService.list().size();

		// check if the CFP was updated
		MvcResult response = mockMvc.perform(get("/callforproposals/read/1")).andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		CFP cv = (CFP) cfpValue;

		assertNotNull("failure - not null", cfpValue);
		// number of CFPs should have not increased
		assertEquals("failure - Number CFPs increased", numberOfCfpPriorToUpdate, numberOfCfpAfterUpdate);
		assertEquals("failure - ID attribute not match", 1, cv.getId());
		assertEquals("failure - Title attribute not match", "Test CFP 1", cv.getTitle());
		assertEquals("failure - Details attribute not match", "Modified details", cv.getDetails());
		assertEquals("failure - Description attribute not match", "Test CFP Description 1", cv.getDescription());
		assertEquals("failure - Repository attribute not match", "Test Repo 1", cv.getRepo().getTitle());
	}

	@Test
	@WithUserDetails("repoowner@rmit.edu.au")
	public void CfpDeleteTest() throws Exception {
		long numberOfCfpPriorToDelete = cfpService.list().size();

		// check if the CFP is existing
		MvcResult response = mockMvc.perform(get("/callforproposals/read/2")).andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		CFP cv = (CFP) cfpValue;
		assertNotNull("failure - repo not existing", cfpValue);
		assertEquals("failure - repo not existing", "Test CFP 2", cv.getTitle());

		// check for the associated Proposal
		MvcResult responseProposal = mockMvc.perform(get("/proposal/read/2")).andReturn();
		Object proposalValue = responseProposal.getModelAndView().getModel().get("proposal");
		Proposal pv = (Proposal) proposalValue;
		assertNotNull("failure - Proposal not existing", proposalValue);
		assertEquals("failure - Proposal not existing", "Test Proposal 2", pv.getTitle());
		// validate association with the CFP
		assertEquals("failure - Proposal not associated with the CFP", "Test CFP 2", pv.getCfp().getTitle());

		// delete CFP 2
		this.mockMvc.perform(get("/callforproposals/deleteconfirmed/2")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/callforproposals/list"));

		long numberOfCfpAfterToDelete = cfpService.list().size();

		// check if the CFP was deleted
		CFP cfp = cfpService.findByTitle("Test CFP 2");
		Proposal prop = proposalService.findByTitle("Test Proposal 2");
		// number of CFPs should have decreased
		assertEquals("failure - CFP did not decreased", numberOfCfpPriorToDelete - 1, numberOfCfpAfterToDelete);
		// repository should not be found
		assertNull("failure - CFP is still existing", cfp);
		// associated CFP should not be found
		assertNull("failure - Repository is still existing", prop);
	}

}
