package com.task.infrastructure.profile_stat;

import com.task.infrastructure.profile.ProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "profile_view_stat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class ProfileViewStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private ProfileEntity profileEntity;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ProfileViewStatEntity(ProfileEntity profileEntity) {
        this.profileEntity = profileEntity;
    }

    @Override
    public String toString() {
        return "ProfileViewStatEntity{" +
            "id=" + id +
            ", profileEntity=" + profileEntity +
            ", createdAt=" + createdAt +
            '}';
    }
}
