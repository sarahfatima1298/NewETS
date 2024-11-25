package org.airtribe.ClientOAuth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class TokenLoggerAdvice {
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@ModelAttribute
	public void logToken(OAuth2AuthenticationToken token) {
		if (token != null) {
			OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
					token.getAuthorizedClientRegistrationId(), token.getName());
			if (authorizedClient != null) {
				OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
				System.out.println("Access Token: {} " +  accessToken.getTokenValue());
			} else {
				System.out.println("No authorized client found for registration ID " + token.getAuthorizedClientRegistrationId() + " and principal name {}" +
						token.getName());
			}
		}
	}
}