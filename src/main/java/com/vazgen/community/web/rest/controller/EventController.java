package com.vazgen.community.web.rest.controller;


import com.vazgen.community.security.CommunityUser;
import com.vazgen.community.security.CommunityUserDetails;
import com.vazgen.community.service.EventService;
import com.vazgen.community.service.dto.EventCreateDto;
import com.vazgen.community.service.dto.EventDto;
import com.vazgen.community.service.dto.EventEditDto;
import com.vazgen.community.web.rest.model.EventCreateRequest;
import com.vazgen.community.web.rest.model.EventEditRequest;
import com.vazgen.community.web.rest.model.EventModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Events")
@RestController
@RequestMapping("/events")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @Operation(summary = "Create Event")
  @PostMapping
  public ResponseEntity<URI> create(@RequestBody EventCreateRequest request, @AuthenticationPrincipal CommunityUser user) {
    EventCreateDto createDto = new EventCreateDto();
    createDto.setName(request.getName());
    createDto.setDate(request.getDate());
    createDto.setLocation(request.getLocation());
    Long eventId = eventService.create(createDto, user);
    UriComponents uriComponents =
        UriComponentsBuilder.newInstance().path("/events/{id}").buildAndExpand(eventId);
    return ResponseEntity.created(uriComponents.toUri()).build();

  }

  @Operation(summary = "Get Event")
  @GetMapping("/{id}")
  public ResponseEntity<EventModel> getById(@PathVariable Long id, @AuthenticationPrincipal CommunityUser user) {
    EventDto eventDto = eventService.getById(id);
    EventModel eventModel = new EventModel();
    eventModel.setId(eventDto.getId());
    eventModel.setDate(eventDto.getDate());
    eventModel.setLocation(eventDto.getLocation());
    eventModel.setName(eventDto.getName());
    return ResponseEntity.ok(eventModel);
  }


  @Operation(summary = "Get All events")
  @GetMapping
  public ResponseEntity<List<EventModel>> getAll() {
    List<EventDto> eventDtos = eventService.getAll();
    List<EventModel> events = eventDtos.stream().map(eventDto -> {
      EventModel eventModel = new EventModel();
      eventModel.setId(eventDto.getId());
      eventModel.setDate(eventDto.getDate());
      eventModel.setLocation(eventDto.getLocation());
      eventModel.setName(eventDto.getName());
      return eventModel;
    }).collect(Collectors.toList());
    return ResponseEntity.ok(events);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody EventEditRequest request, @AuthenticationPrincipal CommunityUser user) {
    EventEditDto editDto = new EventEditDto(id);
    editDto.setName(request.getName());
    editDto.setDate(request.getDate());
    editDto.setLocation(request.getLocation());
    eventService.edit(editDto, user);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal CommunityUser user) {
    eventService.delete(id, user);
    return ResponseEntity.ok().build();
  }


}
