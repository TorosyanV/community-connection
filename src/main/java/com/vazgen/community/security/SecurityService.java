package com.vazgen.community.security;


public interface SecurityService {

  String authenticate(String username, String password);

  void register(String username, String password);
}
