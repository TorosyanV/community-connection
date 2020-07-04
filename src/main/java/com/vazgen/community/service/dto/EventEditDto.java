package com.vazgen.community.service.dto;


public class EventEditDto extends EventCreateDto {

  private final Long id;

  public EventEditDto(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

}
