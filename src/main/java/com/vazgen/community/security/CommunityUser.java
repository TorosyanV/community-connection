package com.vazgen.community.security;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class CommunityUser extends User implements CommunityUserDetails {

  private final Long id;

  public CommunityUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;

  }

  @Override
  public Long getId() {
    return id;
  }

}
