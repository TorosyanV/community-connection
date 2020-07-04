package com.vazgen.community.security;

import com.vazgen.community.data.jpa.EntityHasOwner;

public interface UserRightsChecker {

  void checkOwner(EntityHasOwner entity, CommunityUserDetails ownerToCheck);

}
