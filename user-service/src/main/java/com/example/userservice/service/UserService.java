package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    ResponseUser createUser(RequestUser requestUser);
    ResponseUser getUserByUserId(String userId);
    List<ResponseUser> getUserByAll();
}
