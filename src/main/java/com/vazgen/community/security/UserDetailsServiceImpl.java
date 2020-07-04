package com.vazgen.community.security;

import com.vazgen.community.data.jpa.entity.RoleEntity;
import com.vazgen.community.data.jpa.entity.UserEntity;
import com.vazgen.community.data.jpa.repository.RoleRepository;
import com.vazgen.community.data.jpa.repository.UserRepository;
import com.vazgen.community.exception.UserAlreadyExistsException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value = "userDetailsService")
public class UserDetailsServiceImpl implements CommunityUserDetailsService {

  private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
  private static String ROLE_USER = "ROLE_USER";

  private UserRepository userRepository;
  private RoleRepository roleRepository;


  public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public CommunityUser loadUserByUsername(String username) {
    UserEntity userEntity = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found"));

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    for (RoleEntity role : userEntity.getRoles()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
    }
    return new CommunityUser(userEntity.getId(), userEntity.getUsername(), userEntity.getPasswordHash(), grantedAuthorities);

  }

  @Override
  public void register(String username, String passwordHash) throws UserAlreadyExistsException {
    Optional<UserEntity> userToCheck = userRepository.findByUsername(username);
    if (userToCheck.isPresent()) {
      throw new UserAlreadyExistsException(username);
    }
    UserEntity user = new UserEntity();

    user.setUsername(username);
    user.setPasswordHash(passwordHash);
    user.setRoles(Collections.singleton(roleRepository.findByName(ROLE_USER)));
    userRepository.save(user);
  }


}