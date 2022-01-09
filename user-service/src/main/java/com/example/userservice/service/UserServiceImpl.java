package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseOrder;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    @Override
    public ResponseUser createUser(RequestUser requestUser) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = mapper.map(requestUser, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());

        User user = mapper.map(userDto, User.class);
        user.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(user);

        ResponseUser responseUser = mapper.map(user, ResponseUser.class);

        return responseUser;
    }

    @Override
    public ResponseUser getUserByUserId(String userId) {
        User user = userRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("유저 정보를 찾을 수 없습니다."));

        UserDto userDto = mapper.map(user, UserDto.class);
        List<ResponseOrder> orders = new ArrayList<>();
        userDto.setOrders(orders);

        return mapper.map(userDto, ResponseUser.class);
    }

    /**
     * 전체 데이터 반환
     * @return 리스트
     */
    @Override
    public List<ResponseUser> getUserByAll() {
        return userRepository.findAll().stream().map(v -> mapper.map(v, ResponseUser.class)).collect(Collectors.toList());
    }
}
