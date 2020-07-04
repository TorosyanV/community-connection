package com.vazgen.community.data.exception;


import com.vazgen.community.data.jpa.entity.UserEntity;

public class UserFoundException extends EntityNotFoundException {

  public UserFoundException(Long id) {
    super(UserEntity.class, id);
  }


}
