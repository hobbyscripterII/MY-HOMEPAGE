package com.project.homepage.user;

import org.apache.ibatis.annotations.Mapper;

import com.project.homepage.security.MyUserDetails;

@Mapper
public interface UserMapper {
	MyUserDetails userGet(String id);
}
