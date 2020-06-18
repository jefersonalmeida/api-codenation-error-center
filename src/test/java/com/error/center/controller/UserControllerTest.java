package com.error.center.controller;

import com.error.center.dto.LoginDTO;
import com.error.center.dto.TokenDTO;
import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.service.UserService;
import com.error.center.service.impl.UserDetailsServiceImpl;
import com.error.center.util.enums.Role;
import com.error.center.util.jwt.JwtToken;
import com.error.center.util.jwt.JwtUser;
import com.error.center.util.jwt.JwtUserFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final UUID ID = UUID.fromString("53e2a983-5b29-4527-8f59-e64c3b756f15");
    private static final String NAME = "Test";
    private static final String EMAIL = "email@teste.com";
    private static final String PASSWORD = "123456";
    private static final String URL_REGISTER = "/auth/register";
    private static final String URL_LOGIN = "/auth/login";
    private static final String URL_PROFILE = "/profile";

    @MockBean
    UserService userService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthenticationManager authManager;

    @MockBean
    JwtToken jwtToken;
    @MockBean
    JwtUser jwtUser;

    @Autowired
    MockMvc mvc;

    @Test
    public void testRegister() throws Exception {

        BDDMockito.given(userService.save(Mockito.any(User.class))).willReturn(getMockUser());

        mvc.perform(MockMvcRequestBuilders.post(URL_REGISTER)
                .content(getJsonPayload(ID, NAME, EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(EMAIL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(Role.ROLE_ADMIN.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").doesNotExist())
        ;
    }

    @Test
    public void testSaveInvalidUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(URL_REGISTER)
                .content(getJsonPayload(ID, NAME, "email", PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("must be a well-formed email address"))
        ;
    }

    @Test
    public void testProfileUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    public void testProfileOk() throws Exception {
        BDDMockito.given(userService.findByEmail(Mockito.anyString())).willReturn(Optional.of(getMockUser()));

        mvc.perform(MockMvcRequestBuilders.get(URL_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(NAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(EMAIL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(Role.ROLE_ADMIN.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").doesNotExist())
        ;
    }

    @Test
    @WithMockUser(username = EMAIL, password = PASSWORD, roles = "ADMIN")
    public void testLogin() throws Exception {

        JwtUser jwtUser = JwtUserFactory.create(getMockUser());
        TokenDTO tokenDTO = new TokenDTO("BLA BLA BLA");
        tokenDTO.setToken("BEARER TOKEN");
        BDDMockito.given(jwtToken.getToken(userDetailsService.loadUserByUsername(jwtUser.getUsername()))).willReturn(tokenDTO.getToken());

        mvc.perform(MockMvcRequestBuilders.post(URL_LOGIN)
                .content(getJsonPayloadLogin(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.token").value("BEARER TOKEN"))
        ;
    }

    public User getMockUser() {
        User u = new User(ID, NAME, EMAIL, PASSWORD);
        u.setRole(Role.ROLE_ADMIN);
        return u;
    }

    public String getJsonPayload(UUID id, String name, String email, String password) throws JsonProcessingException {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(Role.ROLE_ADMIN.toString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }

    public String getJsonPayloadLogin(String email, String password) throws JsonProcessingException {
        LoginDTO dto = new LoginDTO();
        dto.setEmail(email);
        dto.setPassword(password);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }
}
