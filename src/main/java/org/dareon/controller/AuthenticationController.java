package org.dareon.controller;

import java.text.ParseException;
import java.util.Arrays;
import javax.validation.ValidationException;

import org.apache.commons.lang3.RandomStringUtils;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.service.JTIService;
import org.dareon.service.RoleService;
import org.dareon.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import net.minidev.json.JSONObject;
/**
 * 
 *This is controller class for defining various user authentication with AAF
 */
@Controller
public class AuthenticationController {

	private String aafIssuer = "https://rapid.test.aaf.edu.au";
	private String aafAudience = "https://www.dareon.org";
	private String aafSecretKey = "Hy2unvH&!kc1OQ&|>%+k*5wGELN&lUd^";
	private String aafAttributes = "https://aaf.edu.au/attributes";
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private JTIService jtiService;
	
	
	    @RequestMapping("/auth/jwt")
    public String authenticateFromAAF(@RequestParam(value="assertion", required=true) String assertion, RedirectAttributes redirectAttributes) {
    	
		try {
			
			//Verify token validity, throw exception if invalid
			SignedJWT jwt = SignedJWT.parse(assertion);
			
			//Compare secret key, throw exception if not matched
	    	JWSVerifier verifier = new MACVerifier(aafSecretKey);
			if (!jwt.verify(verifier))
				throw new ValidationException("Warning: Invalid token signature!!!");
			
			JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
			JSONObject json = claimsSet.getJSONObjectClaim(aafAttributes);
			
			//Verify claims, throw exception if invalid
			if (validateJWTClaims(claimsSet) == true) {

				//String edupersontargetedid = (String)(json.get("edupersontargetedid"));
				//String username = edupersontargetedid.split("!")[2];
				
				// Get the other user's information
				String firstname = (String)(json.get("givenname"));
				String lastname = (String)(json.get("surname"));
				String email = (String)(json.get("mail"));
				String affiliation = (String)(json.get("edupersonscopedaffiliation"));
				String jti = claimsSet.getJWTID();
				System.out.println(jti + " " + email);
				
				//Check if JTI has never been used before - replay attack
				if (jtiService.exists(jti))
					throw new ValidationException("Warning: JTI has already been used!!!");
				else {
					
					jtiService.save(jti);
					
					if (userService.findByEmail(email) == null) {
						String password = RandomStringUtils.randomAlphanumeric(25);
						final Role role = roleService.findByName("ROLE_RO");
						final User user = new User();
						user.setFirstName(firstname);
						user.setLastName(lastname);
						user.setPassword(password);
						user.setEmail(email);
						user.setInstitution(affiliation);
						user.setRoles(Arrays.asList(role));
						userService.save(user);
					}
					User userDetails = userService.findByEmail(email);
					
					// Authenticate the user with Spring Security
					Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getEmail(), userDetails.getPassword());
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			    
		}  catch (ParseException | JOSEException | ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("warning", e.getMessage());
			return "redirect:/login";
		} 
		
		return "redirect:/";
    }
    

    /**
     * 
     * @param defines controls the authentication token issued to the user by right issuer
     * @return
     * @throws ValidationException  for invalid user(audience)
     */
    private boolean validateJWTClaims(JWTClaimsSet claimsSet) throws ValidationException {
        
    	if (!claimsSet.getIssuer().equals(this.aafIssuer))
    		throw new ValidationException("Warning: Invalid token Issuer!!!");
    	
    	if (claimsSet.getAudience().isEmpty() || !claimsSet.getAudience().contains(this.aafAudience))
    		throw new ValidationException("Warning: Invalid token Audience!!!");
 /*   	
    	Date now = new Date();
    	Date notBefore = claimsSet.getNotBeforeTime();
    	if (now.getTime() < notBefore.getTime())
    		throw new ValidationException("Warning: Invalid token Before Time!!!");
    	
    	Date expDate = claimsSet.getExpirationTime();
    	if (now.getTime() >= expDate.getTime())
    		throw new ValidationException("Warning: Invalid token Expiration Time!!!");
*/    	
     	return true;
    }
    
}
