package com.vazgen.community.security;

import com.vazgen.community.data.jpa.EntityHasOwner;
import com.vazgen.community.exception.InvalidOwnerExceptionException;
import org.springframework.stereotype.Component;

@Component
public class UserRightsCheckerImpl implements UserRightsChecker {

  @Override
  public void checkOwner(EntityHasOwner entity, CommunityUserDetails ownerToCheck) {
    if (!ownerToCheck.getId().equals(entity.getOwnerId())) {
      throw new InvalidOwnerExceptionException(ownerToCheck.getId(), entity.getId());
    }
  }
}
