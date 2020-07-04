package com.vazgen.community.security;

import com.vazgen.community.security.jwt.JwtTokenProvider;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

  private final AuthenticationManager authenticationManager;

  private final CommunityUserDetailsService userDetailsService;

  private final JwtTokenProvider jwtTokenProvider;

  private final PasswordEncoder passwordEncoder;

  public SecurityServiceImpl(AuthenticationManager authenticationManager, CommunityUserDetailsService userDetailsService,
      JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtTokenProvider = jwtTokenProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String authenticate(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    CommunityUserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return jwtTokenProvider.createToken(username, userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(
        Collectors.toSet()));
  }

  @Override
  public void register(String username, String password) {
    userDetailsService.register(username, passwordEncoder.encode(password));
  }
}
