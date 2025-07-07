package com.example.golfplatform.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`user`")
@AllArgsConstructor
@Getter
@Builder
@Setter
public class User {
    @Id @GeneratedValue
    private Long id;

    private Long kakaoId;
    private String nickname;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;
    @Enumerated(EnumType.STRING)
    private PreferredRegion preferredRegion;
    @Enumerated(EnumType.STRING)
    private AverageScore averageScore;

    private boolean isFirstLogin = true;

    public User(Long kakaoId, String nickname, String profileImageUrl) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    // getter
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    // setter
    public void completeFirstLogin() {
        this.isFirstLogin = false;
    }

    public void updateAdditionalInfo(String phoneNumber, PreferredRegion region, AverageScore score) {
        this.phoneNumber = phoneNumber;
        this.preferredRegion = region;
        this.averageScore = score;
        this.isFirstLogin = false;
    }
}
