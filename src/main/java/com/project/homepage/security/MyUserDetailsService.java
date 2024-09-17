package com.project.homepage.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.homepage.user.UserMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserMapper mapper;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public MyUserDetailsService(UserMapper mapper) {
    	this.mapper = mapper;
	}
    
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MyUserDetails myUserDetails = mapper.userGet(id);
        log.info("myUserDetails = {}", myUserDetails);
        
        if (myUserDetails != null) {
            return myUserDetails;
        } else {
            throw new UsernameNotFoundException("자격 증명에 실패하였습니다.");
        }
    }
}