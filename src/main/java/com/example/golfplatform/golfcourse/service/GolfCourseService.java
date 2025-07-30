package com.example.golfplatform.golfcourse.service;

import com.example.golfplatform.golfcourse.domain.GolfCourse;
import com.example.golfplatform.golfcourse.repository.GolfCourseRepository;
import com.example.golfplatform.golfcourse.request.KakaoLocalRequest;
import com.example.golfplatform.golfcourse.request.KakaoPositionRequest;
import com.example.golfplatform.golfcourse.response.KakaoApiResponse;
import com.example.golfplatform.golfcourse.response.KakaoPositionResponse;
import com.example.golfplatform.golfcourse.utils.KakaoMapClient;
import com.example.golfplatform.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GolfCourseService implements ApplicationRunner {

    private final KakaoMapClient kakaoMapClient;
    private final GolfCourseRepository golfCourseRepository;
    private final ReviewRepository reviewRepository;

    // 전국 주요 행정구역 키워드 목록
    private static final List<String> REGIONS = List.of(
        "서울특별시", "부산광역시", "대구광역시", "인천광역시",
        "광주광역시", "대전광역시", "울산광역시", "세종특별자치시",
        "경기도", "강원도", "충청북도", "충청남도",
        "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"
    );

    /** 애플리케이션 시작 시 한 번만 실행: 쓰기 가능한 트랜잭션 */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("▶ Bulk import of all golf courses started");
        for (String region : REGIONS) {
            int page = 1;
            boolean hasNext = true;
            while (hasNext) {
                int currentPage = page;
                KakaoApiResponse rsp = kakaoMapClient.searchGolfCoursesByLocal(region, page, 15);
                List<KakaoPositionResponse> docs = rsp.documents();
                if (docs.isEmpty()) {
                    hasNext = false;
                    break;
                }
                docs.stream()
                    .filter(doc -> doc.category_name().contains("> 골프장"))
                    .forEach(doc -> {
                        golfCourseRepository.findByNameAndAddress(doc.place_name(), doc.address_name())
                            .orElseGet(() -> {
                                GolfCourse entity = GolfCourse.builder()
                                    .name(doc.place_name())
                                    .address(doc.address_name())
                                    .lat(Double.parseDouble(doc.y()))
                                    .lng(Double.parseDouble(doc.x()))
                                    .phone(doc.phone())
                                    .region(region)
                                    .build();
                                log.debug("Saved golf course: {} ({}, page {})",
                                    entity.getName(), region, currentPage);
                                return golfCourseRepository.save(entity);
                            });
                    });
                hasNext = !rsp.meta().is_end();
                page++;
            }
        }
        log.info("▶ Bulk import of all golf courses completed");
    }

    /** 현재 위치 기준 검색 → 읽기 전용 트랜잭션 */
    @Transactional
    public List<KakaoPositionResponse> searchByPosition(KakaoPositionRequest req) {
        KakaoApiResponse rsp = kakaoMapClient.searchGolfCoursesByPosition(
            req.lat(), req.lng(), req.radius(), 1, 15
        );

        return rsp.documents().stream()
            .filter(doc -> doc.category_name().contains("> 골프장"))
            .map(doc -> {
                // DB에 없으면 저장, 있으면 조회
                GolfCourse course = golfCourseRepository
                    .findByNameAndAddress(doc.place_name(), doc.address_name())
                    .orElseGet(() -> golfCourseRepository.save(
                        GolfCourse.builder()
                            .name(doc.place_name())
                            .address(doc.address_name())
                            .lat(Double.parseDouble(doc.y()))
                            .lng(Double.parseDouble(doc.x()))
                            .phone(doc.phone())
                            .region(extractRegion(doc.category_name()))
                            .build()
                    ));

                double avg = reviewRepository
                    .findAverageRatingByCourseId(course.getId());

                return new KakaoPositionResponse(
                    doc.address_name(),
                    doc.category_name(),
                    doc.distance(),
                    doc.phone(),
                    doc.place_name(),
                    doc.place_url(),
                    doc.road_address_name(),
                    doc.x(),
                    doc.y(),
                    avg
                );
            })
            .toList();
    }


    /** 지역 키워드 기준 검색 → 읽기 전용 트랜잭션 */
    @Transactional(readOnly = true)
    public List<KakaoPositionResponse> searchByLocal(KakaoLocalRequest req) {
        KakaoApiResponse rsp = kakaoMapClient.searchGolfCoursesByLocal(
            req.Local(), 1, 15
        );
        return rsp.documents().stream()
            .filter(doc -> doc.category_name().contains("> 골프장"))
            .map(doc -> {
                GolfCourse course = golfCourseRepository
                    .findByNameAndAddress(doc.place_name(), doc.address_name())
                    .orElseThrow(() -> new IllegalStateException("미등록 골프장: " + doc.place_name()));
                double avg = reviewRepository.findAverageRatingByCourseId(course.getId());
                return new KakaoPositionResponse(
                    doc.address_name(),
                    doc.category_name(),
                    doc.distance(),
                    doc.phone(),
                    doc.place_name(),
                    doc.place_url(),
                    doc.road_address_name(),
                    doc.x(),
                    doc.y(),
                    avg
                );
            })
            .toList();
    }

    // 카테고리명에서 “> 골프장” 뒤의 지역명만 뽑아내는 유틸
    private String extractRegion(String categoryName) {
        String[] parts = categoryName.split(">");
        return parts[parts.length - 1].trim();
    }
}