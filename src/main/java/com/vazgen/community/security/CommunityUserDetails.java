package com.vazgen.community.security;

import org.springframework.security.core.userdetails.UserDetails;


public interface CommunityUserDetails extends UserDetails {

  Long getId();
}
