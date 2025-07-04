package com.example.golfplatform.user.service;

import com.example.golfplatform.oauth.response.KakaoUserResponse;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import com.example.golfplatform.user.request.AdditionalInfoRequest;
import com.example.golfplatform.user.response.MyInfoResponse;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void addProfile(Long userId, AdditionalInfoRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("유저 없음"));
        if(!user.isFirstLogin()) {
            throw new IllegalStateException("이미 사용자 정보를 입력했습니다.");
        }
        user.setPhoneNumber(request.phoneNumber());
        user.setEmail(request.email());
        user.setPreferredRegion(request.preferredRegion());
        user.setAverageScore(request.averageScore());
        user.completeFirstLogin(); // 최초 로그인 이후 false로 전환
        userRepository.save(user);
    }


}
