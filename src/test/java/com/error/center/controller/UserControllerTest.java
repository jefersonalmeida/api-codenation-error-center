package com.error.center.controller;

import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.service.UserService;
import com.error.center.util.enums.RoleEnum;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final UUID ID = UUID.fromString("53e2a983-5b29-4527-8f59-e64c3b756f15");
    private static final String EMAIL = "email@teste.com";
    private static final String PASSWORD = "123456";
    private static final String URL_REGISTER = "/auth/register";

    @MockBean
    UserService service;

    @Autowired
    MockMvc mvc;

    @Test
    public void testRegister() throws Exception {

        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());

        mvc.perform(MockMvcRequestBuilders.post(URL_REGISTER)
                .content(getJsonPayload(ID, EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(EMAIL))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(RoleEnum.ROLE_ADMIN.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.password").doesNotExist())
        ;
    }

    @Test
    public void testSaveInvalidUser() throws Exception {
        BDDMockito.given(service.save(Mockito.any(User.class))).willReturn(getMockUser());

        mvc.perform(MockMvcRequestBuilders.post(URL_REGISTER)
                .content(getJsonPayload(ID, "email", PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]").value("must be a well-formed email address"))
        ;
    }

    public User getMockUser() {
        User u = new User();
        u.setId(ID);
        u.setEmail(EMAIL);
        u.setPassword(PASSWORD);
        u.setRole(RoleEnum.ROLE_ADMIN);
        return u;
    }

    public String getJsonPayload(UUID id, String email, String password) throws JsonProcessingException {
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(RoleEnum.ROLE_ADMIN.toString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }
}
