package org.dareon;

import org.dareon.domain.CFP;
import org.dareon.domain.Proposal;
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
public class ProposalServiceWebLayerTest {

	@Autowired
	ProposalService proposalService;

	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	@Test
	@WithUserDetails("dataowner@rmit.edu.au")
	public void ProposalCreateAndReadWebTest() throws Exception {
		long numberOfProposalsPriorToCreate = proposalService.list().size();

		this.mockMvc
				.perform(post("/proposal/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("title", "sample proposal 3")
						.param("details", "Sample details for proposal 3")
						.param("description", "Sample description for proposal 3").param("cfp", "1")) // assign to CFP:
																										// Test CFP 1
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/3"));

		long numberOfProposalsAfterCreate = proposalService.list().size();

		// check if the Proposal was created
		MvcResult response = mockMvc.perform(get("/proposal/read/3")).andExpect(status().isOk())
				.andExpect(view().name("proposal/read")).andExpect(model().attributeExists("proposal")).andReturn();
		Object proposalValue = response.getModelAndView().getModel().get("proposal");
		assertTrue(proposalValue instanceof Proposal);
		Proposal pv = (Proposal) proposalValue;

		assertNotNull("failure - not null", proposalValue);
		// number of Proposals should have increased
		assertEquals("failure - Proposals did not increased", numberOfProposalsPriorToCreate + 1,
				numberOfProposalsAfterCreate);
		assertEquals("failure - ID attribute not match", 3, pv.getId());
		assertEquals("failure - Title attribute not match", "sample proposal 3", pv.getTitle());
		assertEquals("failure - Details attribute not match", "Sample details for proposal 3", pv.getDetails());
		assertEquals("failure - Description attribute not match", "Sample description for proposal 3",
				pv.getDescription());
		assertEquals("failure - CFP attribute not match", "Test CFP 1", pv.getCfp().getTitle());
	}

	@Test
	@WithUserDetails("dataowner@rmit.edu.au")
	public void ProposalUpdateAndReadWebTest() throws Exception {
		long numberOfProposalsPriorToUpdate = proposalService.list().size();

		this.mockMvc
				.perform(post("/proposal/create").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.accept(MediaType.APPLICATION_JSON).param("id", "1") // search for proposal to update
						.param("title", "Test Proposal 1").param("details", "Modified details")
						.param("description", "Test Proposal Description 1").param("cfp", "1").param("creator", "4"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("read/1")).andReturn();

		long numberOfProposalsAfterUpdate = proposalService.list().size();

		// check if the Proposal was updated
		MvcResult response = mockMvc.perform(get("/proposal/read/1")).andReturn();
		Object proposalValue = response.getModelAndView().getModel().get("proposal");
		Proposal pv = (Proposal) proposalValue;

		assertNotNull("failure - not null", proposalValue);
		// number of Proposals should have not increased
		assertEquals("failure - Proposals did not increased", numberOfProposalsPriorToUpdate,
				numberOfProposalsAfterUpdate);
		assertEquals("failure - ID attribute not match", 1, pv.getId());
		assertEquals("failure - Title attribute not match", "Test Proposal 1", pv.getTitle());
		assertEquals("failure - Details attribute not match", "Modified details", pv.getDetails());
		assertEquals("failure - Description attribute not match", "Test Proposal Description 1", pv.getDescription());
		assertEquals("failure - CFP attribute not match", "Test CFP 1", pv.getCfp().getTitle());
	}

	@Test
	@WithUserDetails("dataowner@rmit.edu.au")
	public void ProposalDeleteTest() throws Exception {
		long numberOfProposalsPriorToDelete = proposalService.list().size();

		// check if the Proposal is existing
		MvcResult responseProposal = mockMvc.perform(get("/proposal/read/2")).andReturn();
		Object proposalValue = responseProposal.getModelAndView().getModel().get("proposal");
		Proposal pv = (Proposal) proposalValue;
		assertNotNull("failure - Proposal not existing", proposalValue);
		assertEquals("failure - Proposal not existing", "Test Proposal 2", pv.getTitle());

		// delete Proposal 2
		this.mockMvc.perform(get("/proposal/deleteconfirmed/2")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/proposal/list"));

		long numberOfProposalsAfterDelete = proposalService.list().size();

		// check if the Proposal was deleted
		Proposal prop = proposalService.findByTitle("Test Proposal 2");
		// number of repositories should have decreased
		assertEquals("failure - Proposals did not decreased", numberOfProposalsPriorToDelete - 1,
				numberOfProposalsAfterDelete);
		// Proposal should not be found
		assertNull("failure - Repository is still existing", prop);
	}

	@Test
	@WithUserDetails("dataowner@rmit.edu.au")
	public void dataownerViewCfpWebTest() throws Exception {

		// check if the CFP was created
		MvcResult response = mockMvc.perform(get("/callforproposals/read/1")).andExpect(status().isOk())
				.andExpect(view().name("callforproposals/read")).andExpect(model().attributeExists("callForProposals"))
				.andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		assertTrue(cfpValue instanceof CFP);
		CFP cv = (CFP) cfpValue;

		assertNotNull("failure - not null", cfpValue);
		// number of CFPs should have increased
		assertEquals("failure - ID attribute not match", 1, cv.getId());
		assertEquals("failure - Title attribute not match", "Test CFP 1", cv.getTitle());
		assertEquals("failure - Details attribute not match", "Test CFP Details 1", cv.getDetails());
		assertEquals("failure - Description attribute not match", "Test CFP Description 1", cv.getDescription());
		assertEquals("failure - Repository attribute not match", "Test Repo 1", cv.getRepo().getTitle());
	}

}
