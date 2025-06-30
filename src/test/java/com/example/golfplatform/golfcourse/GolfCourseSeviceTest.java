package com.example.golfplatform.golfcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.golfplatform.golfcourse.request.KakaoPositionRequest;
import com.example.golfplatform.golfcourse.response.KakaoApiResponse;
import com.example.golfplatform.golfcourse.response.KakaoPositionResponse;
import com.example.golfplatform.golfcourse.service.GolfCourseService;
import com.example.golfplatform.golfcourse.utils.KakaoMapClient;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GolfCourseSeviceTest {

    @Mock
    private KakaoMapClient kakaoMapClient;

    @InjectMocks
    private GolfCourseService golfCourseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("현재 위치에 대한 골프장 리스트 반환 성공 테스트")
    void golfCourseListByCurrentPosition() {
        // given
        KakaoPositionRequest request = new KakaoPositionRequest(37.5665, 126.9780, 20000);
        KakaoPositionResponse response1 = new KakaoPositionResponse("서울 중구", "골프장", "1000", "02-1234-5678", "중앙 골프장", "http://place.map.kakao.com/123", "서울 중구 도로명", "126.9780", "37.5665");
        KakaoApiResponse mockResponse = new KakaoApiResponse(List.of(response1));

        when(kakaoMapClient.searchGolfCourses(request)).thenReturn(mockResponse);

        // when
        List<KakaoPositionResponse> result = golfCourseService.findNearbyGolfCourses(request);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).place_name()).isEqualTo("중앙 골프장");
    }

    
}
