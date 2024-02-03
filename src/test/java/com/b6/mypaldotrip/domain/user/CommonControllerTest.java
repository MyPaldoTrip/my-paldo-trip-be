package com.b6.mypaldotrip.domain.user;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.b6.mypaldotrip.global.config.VersionConfig;
import com.b6.mypaldotrip.global.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest
@AutoConfigureRestDocs
public class CommonControllerTest implements CommonTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired private WebApplicationContext context;
    @Autowired private RestDocumentationContextProvider restDocumentation;
    @MockBean protected VersionConfig versionConfig;

    @BeforeEach
    void setUp() {
        UserDetailsImpl testUserDetails = new UserDetailsImpl(TEST_USER);
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                testUserDetails, null, testUserDetails.getAuthorities()));
        mockMvc =
                MockMvcBuilders.webAppContextSetup(context)
                        .apply(documentationConfiguration(restDocumentation))
                        .build();
        given(versionConfig.getVersion()).willReturn("v1");
    }
}
