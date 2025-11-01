package com.dinhhuan.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailsSevice {
    UserDetails loadUserById(long userId) ;
}
