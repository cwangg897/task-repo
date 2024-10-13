package com.task.infrastructure.profile_stat;

import com.task.infrastructure.profile.ProfileEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "profile_view_stat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileViewStatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "profile_id")
    private ProfileEntity profileViewStat;

}
