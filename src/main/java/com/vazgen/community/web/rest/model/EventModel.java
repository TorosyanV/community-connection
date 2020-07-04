package com.vazgen.community.web.rest.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
@Schema(name = "Event")
public class EventModel {

  private Long id;
  private String name;
  private LocalDateTime date;
  private String location;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
