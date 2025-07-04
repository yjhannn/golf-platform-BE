package com.example.golfplatform.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.golfplatform.user.domain.AverageScore;
import com.example.golfplatform.user.domain.PreferredRegion;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import com.example.golfplatform.user.request.AdditionalInfoRequest;
import com.example.golfplatform.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    @BeforeEach
    void setup() {
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("최초 로그인 사용자 추가 정보 입력 성공")
    void addAdditionalInfo() {
        // given
        User user = User.builder()
            .isFirstLogin(true)
            .build();
        userRepository.save(user);
        AdditionalInfoRequest request = new AdditionalInfoRequest("01012345678",
            "test@example.com", PreferredRegion.GYEONGSANG, AverageScore.BETWEEN_81_90);

        // when
        userService.updateProfile(user.getId(), request);

        // then
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(updatedUser.getAverageScore()).isEqualTo(AverageScore.BETWEEN_81_90);
        assertThat(updatedUser.getPreferredRegion()).isEqualTo(PreferredRegion.GYEONGSANG);
        assertThat(updatedUser.isFirstLogin()).isFalse();
    }

    @Test
    @DisplayName("추가 정보 입력 실패 - 존재하지 않는 사용자")
    void completeFirstLogin_fail_invalidUser() {
        // given
        Long invalidUserId = 9999L;
        AdditionalInfoRequest request = new AdditionalInfoRequest(
            "01012345678",
            "test@example.com",
            PreferredRegion.GYEONGGI,
            AverageScore.BETWEEN_81_90
        );

        // when & then
        assertThatThrownBy(() -> userService.updateProfile(invalidUserId, request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("유저 없음");
    }

    @Test
    @DisplayName("추가 정보 입력 실패 - 이미 입력된 사용자")
    void completeFirstLogin_fail_alreadyCompleted() {
        // given
        User user = User.builder()
            .kakaoId(123456L)
            .email("test@example.com")
            .nickname("testuser")
            .isFirstLogin(false)
            .build();
        userRepository.save(user);

        AdditionalInfoRequest request = new AdditionalInfoRequest(
            "01012345678",
            "test@example.com",
            PreferredRegion.GYEONGGI,
            AverageScore.BETWEEN_81_90
        );

        // when & then
        assertThatThrownBy(() -> userService.updateProfile(user.getId(), request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("이미 사용자 정보를 입력했습니다.");
    }
}
