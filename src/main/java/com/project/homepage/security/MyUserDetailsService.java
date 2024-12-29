package com.project.homepage.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.homepage.user.UserMapper;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserMapper mapper;

    public MyUserDetailsService(UserMapper mapper) {
    	this.mapper = mapper;
	}
    
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MyUserDetails myUserDetails = mapper.userGet(id);
        
        if (myUserDetails != null) {
            return myUserDetails;
        } else {
            throw new UsernameNotFoundException("자격 증명에 실패하였습니다.");
        }
    }
    
	public String getRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) authentication.getAuthorities();
		
		return authorities.stream()
						  .findFirst()
						  .map(GrantedAuthority :: getAuthority)
						  .orElse(null);
	}
}