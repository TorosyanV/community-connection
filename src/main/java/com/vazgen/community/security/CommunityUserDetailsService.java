package com.vazgen.community.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CommunityUserDetailsService extends UserDetailsService {

  CommunityUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

  void register(String username, String password) throws SecurityException;

}
