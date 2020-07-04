package com.vazgen.community.service;

import com.vazgen.community.data.exception.EventFoundException;
import com.vazgen.community.data.exception.UserFoundException;
import com.vazgen.community.data.jpa.entity.EventEntity;
import com.vazgen.community.data.jpa.repository.EventRepository;
import com.vazgen.community.data.jpa.repository.UserRepository;
import com.vazgen.community.security.CommunityUserDetails;
import com.vazgen.community.security.UserRightsChecker;
import com.vazgen.community.service.dto.EventCreateDto;
import com.vazgen.community.service.dto.EventDto;
import com.vazgen.community.service.dto.EventEditDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {


  private final EventRepository eventRepository;
  private final UserRepository userRepository;

  private final UserRightsChecker userRightsChecker;

  public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
      UserRightsChecker userRightsChecker) {
    this.eventRepository = eventRepository;
    this.userRepository = userRepository;
    this.userRightsChecker = userRightsChecker;
  }

  @Override
  @Transactional
  public Long create(EventCreateDto event, CommunityUserDetails user) {
    EventEntity eventEntity = new EventEntity();
    eventEntity.setName(event.getName());
    eventEntity.setDate(event.getDate());
    eventEntity.setLocation(event.getLocation());
    eventEntity.setUser(userRepository.findById(user.getId()).orElseThrow(() -> new UserFoundException(user.getId())));
    eventRepository.save(eventEntity);
    return eventEntity.getId();
  }

  @Override
  @Transactional
  public void edit(EventEditDto event, CommunityUserDetails user) {
    EventEntity eventEntity = eventRepository.findByIdAndDeletedIsFalse(event.getId()).orElseThrow(() -> new EventFoundException(event.getId()));
    userRightsChecker.checkOwner(eventEntity, user);
    eventEntity.setName(event.getName());
    eventEntity.setDate(event.getDate());
    eventEntity.setLocation(event.getLocation());
    eventRepository.save(eventEntity);

  }

  @Override
  @Transactional(readOnly = true)
  public EventDto getById(Long id) {
    EventEntity eventEntity = eventRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new EventFoundException(id));
    EventDto event = new EventDto();
    event.setId(eventEntity.getId());
    event.setName(eventEntity.getName());
    event.setDate(eventEntity.getDate());
    event.setLocation(eventEntity.getLocation());
    return event;
  }

  @Override
  @Transactional(readOnly = true)
  public List<EventDto> getAll() {
    List<EventEntity> all = eventRepository.findAllByDeletedIsFalse();
    return all.stream().map(ev -> {
      EventDto event = new EventDto();
      event.setId(ev.getId());
      event.setName(ev.getName());
      event.setDate(ev.getDate());
      event.setLocation(ev.getLocation());
      return event;
    }).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void delete(Long id, CommunityUserDetails user) {
    EventEntity event = eventRepository.findByIdAndDeletedIsFalse(id).orElseThrow(() -> new EventFoundException(id));
    userRightsChecker.checkOwner(event, user);
    event.setDeleted(true);
    eventRepository.save(event);
  }
}
