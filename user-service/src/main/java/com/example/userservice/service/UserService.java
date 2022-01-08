package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;

public interface UserService {
    ResponseUser createUser(RequestUser requestUser);
}
