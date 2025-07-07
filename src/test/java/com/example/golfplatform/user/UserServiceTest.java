package com.example.golfplatform.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.golfplatform.user.domain.AverageScore;
import com.example.golfplatform.user.domain.PreferredRegion;
import com.example.golfplatform.user.domain.User;
import com.example.golfplatform.user.repository.UserRepository;
import com.example.golfplatform.user.request.AdditionalInfoRequest;
import com.example.golfplatform.user.request.UpdateMyInfoRequest;
import com.example.golfplatform.user.response.MyInfoResponse;
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
        userService.addProfile(user.getId(), request);

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
        assertThatThrownBy(() -> userService.addProfile(invalidUserId, request))
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
        assertThatThrownBy(() -> userService.addProfile(user.getId(), request))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("이미 사용자 정보를 입력했습니다.");
    }

    @Test
    @DisplayName("마이페이지 - 사용자 정보 조회 성공")
    void getUserProfile() {
        User user = User.builder()
            .kakaoId(123L)
            .nickname("테스트유저")
            .email("test@email.com")
            .phoneNumber("01012345678")
            .preferredRegion(PreferredRegion.JEJU)
            .averageScore(AverageScore.OVER_100)
            .isFirstLogin(false)
            .build();
        userRepository.save(user);

        // when
        MyInfoResponse response = userService.getMyInfo(user.getId());

        // then
        assertThat(response.nickname()).isEqualTo("테스트유저");
        assertThat(response.email()).isEqualTo("test@email.com");
        assertThat(response.phoneNumber()).isEqualTo("01012345678");
        assertThat(response.preferredRegion()).isEqualTo("제주도");
        assertThat(response.averageScore()).isEqualTo("100타 이상");
    }

    @Test
    @DisplayName("마이페이지 - 존재하지 않는 유저 정보 조회 시 예외 발생")
    void getUserProfile_Fail() {
        // when & then
        assertThatThrownBy(() -> userService.getMyInfo(9999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("유저 없음");

    }

    @Test
    @DisplayName("마이페이지 - 사용자 정보 수정 성공")
    void updateMyInfo_success() {
        // given
        User user = User.builder()
            .kakaoId(456L)
            .nickname("초기유저")
            .email("init@email.com")
            .phoneNumber("01000000000")
            .preferredRegion(PreferredRegion.JEJU)
            .averageScore(AverageScore.UNDER_72)
            .isFirstLogin(false)
            .build();
        userRepository.save(user);

        UpdateMyInfoRequest request = new UpdateMyInfoRequest(
            "01099998888",
            "강원도",
            "72~80타"
        );

        // when
        userService.updateMyInfo(user.getId(), request);

        // then
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getPhoneNumber()).isEqualTo("01099998888");
        assertThat(updatedUser.getPreferredRegion()).isEqualTo(PreferredRegion.GANGWON);
        assertThat(updatedUser.getAverageScore()).isEqualTo(AverageScore.BETWEEN_72_80);
    }

    @Test
    @DisplayName("마이페이지 - 잘못된 선호 지역 입력 시 예외 발생")
    void updateMyInfo_fail_invalidPreferredRegion() {
        // given
        User user = userRepository.save(User.builder()
            .kakaoId(111L)
            .nickname("invalidRegionUser")
            .email("test@naver.com")
            .phoneNumber("01012345678")
            .preferredRegion(PreferredRegion.GYEONGGI)
            .averageScore(AverageScore.OVER_100)
            .isFirstLogin(false)
            .build());

        UpdateMyInfoRequest request = new UpdateMyInfoRequest(
            "01011112222",
            "무효지역",  // 존재하지 않는 지역
            "81~90타"
        );

        // when & then
        assertThatThrownBy(() -> userService.updateMyInfo(user.getId(), request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("잘못된 지역입니다: 무효지역");
    }
}
