package com.project.homepage.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.homepage.cmmn.Const;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyUserDetails implements UserDetails {
    private int iadmin;
    private String id;
    private String pw;
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(Const.ROLE_PREFIX + role));
        return list;
    }

    public int getIadmin() {return iadmin;}
    @Override public String getPassword() {return pw;}
    @Override public String getUsername() {return id;}
    @Override public boolean isAccountNonExpired() {return true;}
    @Override public boolean isAccountNonLocked() {return true;}
    @Override public boolean isCredentialsNonExpired() {return true;}
    @Override public boolean isEnabled() {return true;}
}