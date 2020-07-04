package com.vazgen.community.web.rest.controller;

import com.vazgen.community.exception.UserAlreadyExistsException;
import com.vazgen.community.security.SecurityService;
import com.vazgen.community.web.rest.model.AuthenticationRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final SecurityService securityService;

  public AuthenticationController(SecurityService securityService) {
    this.securityService = securityService;
  }

  @PostMapping("/login")
  public ResponseEntity login(@RequestBody AuthenticationRequest data) {

    try {
      String token = securityService.authenticate(data.getUsername(), data.getPassword());

      Map<Object, Object> model = new HashMap<>();
      model.put("username", data.getUsername());
      model.put("token", token);
      return ResponseEntity.ok(model);
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username or password");
    }
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody AuthenticationRequest data) {
    try {
      securityService.register(data.getUsername(), data.getPassword());
    } catch (UserAlreadyExistsException e) {
      ResponseEntity.badRequest();
    }
    return ResponseEntity.ok().build();
  }
}