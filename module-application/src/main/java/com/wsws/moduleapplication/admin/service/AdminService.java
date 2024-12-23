package com.wsws.moduleapplication.admin.service;

import com.wsws.moduleapplication.usercontext.user.exception.UserNotFoundException;
import com.wsws.moduledomain.usercontext.user.aggregate.User;
import com.wsws.moduledomain.usercontext.user.repo.UserRepository;
import com.wsws.moduledomain.usercontext.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;;


    public void deactivateUser(String userId){
        User user = findUserByIdOrThrow(userId);
        user.deactivate();

        userRepository.save(user);
    }

    public void activateUser(String userId){
        User user = findUserByIdOrThrow(userId);
        user.activate();

        userRepository.save(user);
    }




    private User findUserByIdOrThrow(String userId) {
        return userRepository.findById(UserId.of(userId))
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
    }


}
