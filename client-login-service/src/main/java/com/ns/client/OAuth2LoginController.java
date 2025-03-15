package com.ns.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Slf4j
@Controller
public class OAuth2LoginController {

    @GetMapping("/")
    public String index(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                        @AuthenticationPrincipal OAuth2User oauth2User) {
        log.info(oauth2User.toString());
        log.info("TOKEN-TYPE:{}, TOKEN:{} ", authorizedClient.getAccessToken().getTokenType().getValue()
                , authorizedClient.getAccessToken().getTokenValue());
        log.info(Objects.requireNonNull(authorizedClient.getRefreshToken()).getTokenValue());
        model.addAttribute("userName", oauth2User.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        model.addAttribute("userAccessToken", authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("userRefreshToken", authorizedClient.getRefreshToken().getTokenValue());
        return "index";
    }

}