package com.ns.resource_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@Import(SecurityConfiguration.class)
class MessageControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void indexGreetsAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/")
                        .with(jwt().jwt(jwt -> jwt.subject("jwtSub"))))
                .andExpect(content().string(is("Hello, jwtSub!")));

    }

    @Test
    void messageCanBeReadWithScopeMessageReadAuthority() throws Exception {

        this.mockMvc.perform(get("/message")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "message:read"))))
                .andExpect(content().string(is("secret message")));

        this.mockMvc.perform(get("/message")
                        .with(jwt().authorities(new SimpleGrantedAuthority(("SCOPE_message:read")))))
                .andExpect(content().string(is("secret message")));

    }

    @Test
    void messageCanNotBeReadWithoutScopeMessageReadAuthority() throws Exception {

        this.mockMvc.perform(get("/message")
                        .with(jwt()))
                .andExpect(status().isForbidden());

    }

    @Test
    void messageCanNotBeCreatedWithoutAnyScope() throws Exception {

        this.mockMvc.perform(post("/message")
                        .content("Hello message")
                        .with(jwt()))
                .andExpect(status().isForbidden());

    }

    @Test
    void messageCanNotBeCreatedWithScopeMessageReadAuthority() throws Exception {

        this.mockMvc.perform(post("/message")
                        .content("Hello message")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "message:read"))))
                .andExpect(status().isForbidden());

    }

    @Test
    void messageCanBeCreatedWithScopeMessageWriteAuthority() throws Exception {

        this.mockMvc.perform(post("/message")
                        .content("Hello message")
                        .with(jwt().jwt(jwt -> jwt.claim("scope", "message:write"))))
                .andExpect(status().isOk())
                .andExpect(content().string(is("Message was created. Content: Hello message")));

    }
}