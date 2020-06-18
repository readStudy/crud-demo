package com.practice.security.email;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public final class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	
	public static final String SPRING_SECURITY_FORM_EMAIL_CODE_KEY = "email_code";
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	
    private String emialCodeParameter = SPRING_SECURITY_FORM_EMAIL_CODE_KEY;
    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    
    private boolean postOnly = true;
    
    public EmailCodeAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/email", "POST"));
    }

    private String obtainEmailCode(HttpServletRequest request) {
        return request.getParameter(this.emialCodeParameter);
    }
    
    private String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    private void setDetails(HttpServletRequest request, EmailCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
    
    public void setPostOnly(boolean postOnly) {
    	this.postOnly = postOnly;
    }
    
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
        	
        	Map<String, String> emailCodeInSession = (Map<String, String>)request.getSession().getAttribute("emailCode");
        	
            String emailCode = this.obtainEmailCode(request);
            String username = obtainUsername(request);    
            
            if (emailCode == null) {
            	emailCode = "";
            }
            
    		if (username == null) {
    			username = "";
    		}
            
            emailCode = emailCode.trim();
            username = username.trim();
            
            if(!emailCode.equals(emailCodeInSession.get("emailCode")) || !username.equals(emailCodeInSession.get("username"))) {
            	throw new AuthenticationServiceException("登入失敗");
            }
            
            request.getSession().removeAttribute("emailCode");
            EmailCodeAuthenticationToken authRequest = new EmailCodeAuthenticationToken(username, emailCode);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
	}
}
