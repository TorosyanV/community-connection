package com.vazgen.community;


import static java.util.function.Predicate.isEqual;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazgen.community.data.jpa.entity.EventEntity;
import com.vazgen.community.data.jpa.repository.EventRepository;
import com.vazgen.community.web.rest.model.AuthenticationRequest;
import com.vazgen.community.web.rest.model.EventCreateRequest;
import com.vazgen.community.web.rest.model.EventEditRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.tomcat.util.file.Matcher;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

@ContextConfiguration(classes = Application.class)
@Transactional
@SpringBootTest
@WebAppConfiguration
public class UserTest {


  private MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private WebApplicationContext ctx;


  @BeforeEach
  public void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
  }

  @Test
  public void getToken() throws Exception {

    AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setUsername("user");
    authenticationRequest.setPassword("password");

    String json = mapper.writeValueAsString(authenticationRequest);
    mvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user"))
        .andExpect(jsonPath("$.token").exists())
        .andDo(print())
        .andReturn();

  }

  @Test
  public void someRequestWithToken() throws Exception {

    AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setUsername("user");
    authenticationRequest.setPassword("password");

    String json = mapper.writeValueAsString(authenticationRequest);
    MvcResult result = mvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user"))
        .andExpect(jsonPath("$.token").exists())
        .andDo(print())
        .andReturn();
    String contentAsString = result.getResponse().getContentAsString();

    JsonNode jsonObject = mapper.readValue(contentAsString, JsonNode.class);
    String token = jsonObject.get("token").asText();

    mvc.perform(delete("/events/1")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
//        .andExpect(authenticated().withRoles("USER"))
        .andReturn();

  }


}
