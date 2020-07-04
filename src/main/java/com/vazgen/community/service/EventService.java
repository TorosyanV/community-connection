package com.vazgen.community.service;

import com.vazgen.community.security.CommunityUserDetails;
import com.vazgen.community.service.dto.EventCreateDto;
import com.vazgen.community.service.dto.EventDto;
import com.vazgen.community.service.dto.EventEditDto;
import java.util.List;

public interface EventService {

  Long create(EventCreateDto event, CommunityUserDetails user);

  void edit(EventEditDto event, CommunityUserDetails user);

  EventDto getById(Long id);

  List<EventDto> getAll();

  void delete(Long id, CommunityUserDetails user);
}
