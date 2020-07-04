package com.vazgen.community;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazgen.community.data.jpa.entity.EventEntity;
import com.vazgen.community.data.jpa.repository.EventRepository;
import com.vazgen.community.web.rest.model.EventCreateRequest;
import com.vazgen.community.web.rest.model.EventEditRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class EventTest {


  private MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @Autowired
  private WebApplicationContext ctx;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private EventRepository eventRepository;


  @BeforeEach
  public void setUp() {
    this.mvc = MockMvcBuilders.webAppContextSetup(ctx).apply(springSecurity()).build();
  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
  public void create() throws Exception {

    int countBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    EventCreateRequest eventCreateRequest = new EventCreateRequest();
    eventCreateRequest.setName("Event Name");
    eventCreateRequest.setLocation("Some Location Address");
    eventCreateRequest.setDate(LocalDateTime.of(2050, Month.AUGUST, 10, 16, 20));
    String json = mapper.writeValueAsString(eventCreateRequest);
    MvcResult mvcResult = mvc.perform(post("/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern("/events/{[0-9]*}"))
        .andDo(print())
        .andReturn();
    List<String> pathSegments = UriComponentsBuilder.fromPath(mvcResult.getResponse().getRedirectedUrl()).build().getPathSegments();
    String idStr = pathSegments.get(pathSegments.size() - 1);
    Long eventId = Long.valueOf(idStr);

    long countAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    Assertions.assertThat(countAfter - countBefore).isOne();

    EventEntity eventEntity = eventRepository.getOne(eventId);
    Assertions.assertThat(eventEntity.getName()).isEqualTo(eventCreateRequest.getName());
    Assertions.assertThat(eventEntity.getDate()).isEqualTo(eventCreateRequest.getDate());
    Assertions.assertThat(eventEntity.getLocation()).isEqualTo(eventCreateRequest.getLocation());

  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
  public void edit() throws Exception {

    int countBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    EventEditRequest eventCreateRequest = new EventEditRequest();
    eventCreateRequest.setName("Event Name");
    eventCreateRequest.setLocation("Some Location Address");
    eventCreateRequest.setDate(LocalDateTime.of(2050, Month.AUGUST, 10, 16, 20));
    String json = mapper.writeValueAsString(eventCreateRequest);
    mvc.perform(put("/events/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andDo(print()).andReturn();

    long countAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    Assertions.assertThat(countAfter).isEqualTo(countBefore);

    EventEntity eventEntity = eventRepository.getOne(1L);
    Assertions.assertThat(eventEntity.getName()).isEqualTo(eventCreateRequest.getName());
    Assertions.assertThat(eventEntity.getDate()).isEqualTo(eventCreateRequest.getDate());
    Assertions.assertThat(eventEntity.getLocation()).isEqualTo(eventCreateRequest.getLocation());

  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
  public void remove() throws Exception {

    int countBefore = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    mvc.perform(delete("/events/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print()).andReturn();

    long countAfter = JdbcTestUtils.countRowsInTable(jdbcTemplate, "event");

    Assertions.assertThat(countAfter).isEqualTo(countBefore);

    EventEntity eventEntity = eventRepository.getOne(1L);
    Assertions.assertThat(eventEntity.isDeleted()).isTrue();

  }


  @Test
  public void getById() throws Exception {

    mvc.perform(get("/events/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"id\":1,\"name\":\"my event\",\"date\":\"2020-07-04T15:51:16\",\"location\":\"Some location Address\"}"))
        .andDo(print()).andReturn();
  }

  @Test
  public void getAll() throws Exception {

    mvc.perform(get("/events")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"name\":\"my event\",\"date\":\"2020-07-04T15:51:16\",\"location\":\"Some location Address\"}]"))
        .andDo(print()).andReturn();
  }

}
