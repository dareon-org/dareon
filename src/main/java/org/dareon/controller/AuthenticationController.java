package org.dareon.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import javax.validation.ValidationException;

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
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


import net.minidev.json.JSONObject;

@Controller
public class AuthenticationController {

	private String aafUrl = "https://rapid.test.aaf.edu.au/jwt/authnrequest/research/SAaWlfVa_TEfjcz4TisRFw";
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
    public String authenticateFromAAF(@RequestParam(value="assertion", required=true) String assertion) {
    	
		try {
			SignedJWT jwt = SignedJWT.parse(assertion);
			
	    	JWSVerifier verifier = new MACVerifier(aafSecretKey);
			if (!jwt.verify(verifier))
				throw new ValidationException("Error: Invalid token signature!!!");
			
			JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
			JSONObject json = claimsSet.getJSONObjectClaim(aafAttributes);
			

			if (validateJWTClaims(claimsSet) == true) {

				String edupersontargetedid = (String)(json.get("edupersontargetedid"));
				String username = edupersontargetedid.split("!")[2];
				
				// Get the other user's information
				String firstname = (String)(json.get("givenname"));
				String lastname = (String)(json.get("surname"));
				String email = (String)(json.get("mail"));
				String affiliation = (String)(json.get("edupersonscopedaffiliation"));
				String jti = claimsSet.getJWTID();
				System.out.println(jti + " " + email);
				if (jtiService.exists(jti))
					throw new ValidationException("Error: JTI already existing!!!");
				else {
					
					jtiService.save(jti);
					
					if (userService.findByEmail(email) == null) {
						final Role role = roleService.findByName("ROLE_RO");
						final User user = new User();
						user.setFirstName(firstname);
						user.setLastName(lastname);
						user.setPassword("");
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
			return "redirect:/login";
		} 

		
		return "redirect:/";
    }
    

    
    private boolean validateJWTClaims(JWTClaimsSet claimsSet) throws ValidationException {
        
    	if (!claimsSet.getIssuer().equals(this.aafIssuer))
    		throw new ValidationException("Error: Invalid token Issuer!!!");
    	
    	if (claimsSet.getAudience().isEmpty() || !claimsSet.getAudience().contains(this.aafAudience))
    		throw new ValidationException("Error: Invalid token Audience!!!");
    	
    	Date now = new Date();
    	Date notBefore = claimsSet.getNotBeforeTime();
    	if (now.getTime() < notBefore.getTime())
    		throw new ValidationException("Error: Invalid token Before Time!!!");
    	
    	Date expDate = claimsSet.getExpirationTime();
    	if (now.getTime() >= expDate.getTime())
    		throw new ValidationException("Error: Invalid token Expiration Time!!!");
    	
     	return true;
    }
  
    
}