package com.vazgen.community.data.exception;


import com.vazgen.community.data.jpa.entity.EventEntity;

public class EventFoundException extends EntityNotFoundException {

  public EventFoundException(Long id) {
    super(EventEntity.class, id);
  }


}
