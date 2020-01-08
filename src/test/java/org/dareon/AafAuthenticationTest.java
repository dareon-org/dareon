package org.dareon;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import javax.annotation.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AafAuthenticationTest {

	public String assertionWithValidClaimAndSignature = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTMwMzEzNTMsIm5iZiI6MTQ5MzAzMTI5MywiZXhwIjoxNDkzMDMxNDczLCJqdGkiOiJiclk3dEpuZE95aGt3anJXbXIwdjFVbUZWU1pHOXJvLSIsInR5cCI6ImF1dGhucmVzcG9uc2UiLCJodHRwczovL2FhZi5lZHUuYXUvYXR0cmlidXRlcyI6eyJjbiI6IlJvbW1lbCBHYWRkaSIsImRpc3BsYXluYW1lIjoiUm9tbWVsIEdhZGRpIiwic3VybmFtZSI6IkdhZGRpIiwiZ2l2ZW5uYW1lIjoiUm9tbWVsIiwibWFpbCI6IlMzNjE5MTczQHN0dWRlbnQucm1pdC5lZHUuYXUiLCJlZHVwZXJzb25zY29wZWRhZmZpbGlhdGlvbiI6InN0dWRlbnRAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb25wcmluY2lwYWxuYW1lIjoiczM2MTkxNzNAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb250YXJnZXRlZGlkIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0sImlzcyI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IiwiYXVkIjoiaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyIsInN1YiI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IWh0dHBzOi8vd3d3LmRhcmVvbi5vcmchaEpKanQ3Q1NIOTFPeXF6Q3JNeXJvdnZjVENnPSJ9.YSlFnsj581I_XbOiNQWMkrZ2BwqMfnYodQcjkEMQctU";

	public String assertionWithValidClaimAndSignature2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTMwMzEzNTMsIm5iZiI6MTQ5MzAzMTI5MywiZXhwIjoxNDkzMDMxNDczLCJqdGkiOiIxMVk3dEpuZE95aGt3anJXbXIwdjFVbUZWU1pHOXJvLSIsInR5cCI6ImF1dGhucmVzcG9uc2UiLCJodHRwczovL2FhZi5lZHUuYXUvYXR0cmlidXRlcyI6eyJjbiI6IlJvbW1lbCBHYWRkaSIsImRpc3BsYXluYW1lIjoiUm9tbWVsIEdhZGRpIiwic3VybmFtZSI6IkdhZGRpIiwiZ2l2ZW5uYW1lIjoiUm9tbWVsIiwibWFpbCI6IlMzNjE5MTczQHN0dWRlbnQucm1pdC5lZHUuYXUiLCJlZHVwZXJzb25zY29wZWRhZmZpbGlhdGlvbiI6InN0dWRlbnRAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb25wcmluY2lwYWxuYW1lIjoiczM2MTkxNzNAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb250YXJnZXRlZGlkIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0sImlzcyI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IiwiYXVkIjoiaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyIsInN1YiI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IWh0dHBzOi8vd3d3LmRhcmVvbi5vcmchaEpKanQ3Q1NIOTFPeXF6Q3JNeXJvdnZjVENnPSJ9.iBde2KvCJPFdCN0LE7gnVYZ-3XZ89iRacYd9vveVeYo";

	public String assertionWithInvalidSignature = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTMwMzEzNTMsIm5iZiI6MTQ5MzAzMTI5MywiZXhwIjoxNDkzMDMxNDczLCJqdGkiOiJiclk3dEpuZE95aGt3anJXbXIwdjFVbUZWU1pHOXJvLSIsInR5cCI6ImF1dGhucmVzcG9uc2UiLCJodHRwczovL2FhZi5lZHUuYXUvYXR0cmlidXRlcyI6eyJjbiI6IlJvbW1lbCBHYWRkaSIsImRpc3BsYXluYW1lIjoiUm9tbWVsIEdhZGRpIiwic3VybmFtZSI6IkdhZGRpIiwiZ2l2ZW5uYW1lIjoiUm9tbWVsIiwibWFpbCI6IlMzNjE5MTczQHN0dWRlbnQucm1pdC5lZHUuYXUiLCJlZHVwZXJzb25zY29wZWRhZmZpbGlhdGlvbiI6InN0dWRlbnRAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb25wcmluY2lwYWxuYW1lIjoiczM2MTkxNzNAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb250YXJnZXRlZGlkIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0sImlzcyI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IiwiYXVkIjoiaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyIsInN1YiI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IWh0dHBzOi8vd3d3LmRhcmVvbi5vcmchaEpKanQ3Q1NIOTFPeXF6Q3JNeXJvdnZjVENnPSJ9.uFmI4P5zSyWJBZl3vVxndCukaB_JoDkXel8b67bJTvs";

	public String assertionWithInvalidIssuer = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTMwMzEzNTMsIm5iZiI6MTQ5MzAzMTI5MywiZXhwIjoxNDkzMDMxNDczLCJqdGkiOiJiclk3dEpuZE95aGt3anJXbXIwdjFVbUZWU1pHOXJvLSIsInR5cCI6ImF1dGhucmVzcG9uc2UiLCJodHRwczovL2FhZi5lZHUuYXUvYXR0cmlidXRlcyI6eyJjbiI6IlJvbW1lbCBHYWRkaSIsImRpc3BsYXluYW1lIjoiUm9tbWVsIEdhZGRpIiwic3VybmFtZSI6IkdhZGRpIiwiZ2l2ZW5uYW1lIjoiUm9tbWVsIiwibWFpbCI6IlMzNjE5MTczQHN0dWRlbnQucm1pdC5lZHUuYXUiLCJlZHVwZXJzb25zY29wZWRhZmZpbGlhdGlvbiI6InN0dWRlbnRAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb25wcmluY2lwYWxuYW1lIjoiczM2MTkxNzNAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb250YXJnZXRlZGlkIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0sImlzcyI6Imh0dHBzOi8vd3d3LmJvZ3VzLmNvbSIsImF1ZCI6Imh0dHBzOi8vd3d3LmRhcmVvbi5vcmciLCJzdWIiOiJodHRwczovL3JhcGlkLnRlc3QuYWFmLmVkdS5hdSFodHRwczovL3d3dy5kYXJlb24ub3JnIWhKSmp0N0NTSDkxT3lxekNyTXlyb3Z2Y1RDZz0ifQ.N52hNPK2RQYayXAKkTiuP1NC6F2y86mz7tX59bIenVY";

	public String assertionWithInvalidAudience = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE0OTMwMzEzNTMsIm5iZiI6MTQ5MzAzMTI5MywiZXhwIjoxNDkzMDMxNDczLCJqdGkiOiJiclk3dEpuZE95aGt3anJXbXIwdjFVbUZWU1pHOXJvLSIsInR5cCI6ImF1dGhucmVzcG9uc2UiLCJodHRwczovL2FhZi5lZHUuYXUvYXR0cmlidXRlcyI6eyJjbiI6IlJvbW1lbCBHYWRkaSIsImRpc3BsYXluYW1lIjoiUm9tbWVsIEdhZGRpIiwic3VybmFtZSI6IkdhZGRpIiwiZ2l2ZW5uYW1lIjoiUm9tbWVsIiwibWFpbCI6IlMzNjE5MTczQHN0dWRlbnQucm1pdC5lZHUuYXUiLCJlZHVwZXJzb25zY29wZWRhZmZpbGlhdGlvbiI6InN0dWRlbnRAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb25wcmluY2lwYWxuYW1lIjoiczM2MTkxNzNAcm1pdC5lZHUuYXUiLCJlZHVwZXJzb250YXJnZXRlZGlkIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0sImlzcyI6Imh0dHBzOi8vcmFwaWQudGVzdC5hYWYuZWR1LmF1IiwiYXVkIjoiaHR0cHM6Ly93d3cuYm9ndXMuY29tIiwic3ViIjoiaHR0cHM6Ly9yYXBpZC50ZXN0LmFhZi5lZHUuYXUhaHR0cHM6Ly93d3cuZGFyZW9uLm9yZyFoSkpqdDdDU0g5MU95cXpDck15cm92dmNUQ2c9In0.WMu1I3WKaBUxdjN7kaeMhpt7S_pjBVTZYtw7JmnpcWM";

	@Resource
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}

	/*
	 * Note: The AuthenticationController requires fresh token therefore there's a
	 * need to temporarily disable the BeforeTime and ExpirationTime validation for
	 * the following tests to work properly with the above tokens.
	 */

	@Test
	public void aafAuthenticationWithNoAssertionTest() throws Exception {

		mockMvc.perform(get("/auth/jwt")).andExpect(status().isBadRequest()).andExpect(status().is4xxClientError());
	}

	@Test
	public void aafAuthenticationWithInvalidAssertionTest() throws Exception {

		RequestBuilder request = get("/auth/jwt").param("assertion", "sample invalid assertion format");

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login")).andExpect(
				flash().attribute("warning", "Invalid serialized unsecured/JWS/JWE object: Missing part delimiters"));
	}

	// Test valid claim sets and signature, allow authentication
	@Test
	public void aafAuthenticationWithValidClaimAndSignatureTest() throws Exception {

		RequestBuilder request = get("/auth/jwt").param("assertion", assertionWithValidClaimAndSignature);

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/")); // allow
																										// authentication
	}

	// Test invalid signature
	@Test
	public void aafAuthenticationWithInvalidSignatureTest() throws Exception {
		RequestBuilder request = get("/auth/jwt").param("assertion", assertionWithInvalidSignature);

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
				.andExpect(flash().attribute("warning", "Warning: Invalid token signature!!!"));
	}

	// Test invalid issuer
	@Test
	public void aafAuthenticationWithInvalidIssuerTest() throws Exception {
		RequestBuilder request = get("/auth/jwt").param("assertion", assertionWithInvalidIssuer);

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
				.andExpect(flash().attribute("warning", "Warning: Invalid token Issuer!!!"));
	}

	// Test invalid audience
	@Test
	public void aafAuthenticationWithInvalidAudienceTest() throws Exception {
		RequestBuilder request = get("/auth/jwt").param("assertion", assertionWithInvalidAudience);

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
				.andExpect(flash().attribute("warning", "Warning: Invalid token Audience!!!"));
	}

	// Test replay attack
	@Test
	public void aafAuthenticationReplayAttackTest() throws Exception {

		RequestBuilder request = get("/auth/jwt").param("assertion", assertionWithValidClaimAndSignature2);

		mockMvc.perform(request).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

		// Replay attack
		RequestBuilder request2 = get("/auth/jwt").param("assertion", assertionWithValidClaimAndSignature2);

		mockMvc.perform(request2).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"))
				.andExpect(flash().attribute("warning", "Warning: JTI has already been used!!!"));
	}

}
