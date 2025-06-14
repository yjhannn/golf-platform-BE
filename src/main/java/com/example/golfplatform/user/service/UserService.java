package com.example.golfplatform.user.service;

import com.example.golfplatform.oauth.response.KakaoUserResponse;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findOrCreateUser(KakaoUserResponse kakaoUser) {
        return userRepository.findByKakaoId(kakaoUser.id())
            .orElseGet(() -> {
                User newUser = new User(kakaoUser.id(), kakaoUser.kakao_account().profile()
                    .nickname(), kakaoUser.kakao_account().profile().profile_image_url());
                return userRepository.save(newUser);
            });
    }


}
