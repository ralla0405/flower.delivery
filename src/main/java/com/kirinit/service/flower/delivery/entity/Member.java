package com.kirinit.service.flower.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", length = 30, unique = true)
    private String username;

    @Column(name = "name", length = 100)
    private String name;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role; // ROLE_USER, ROLE_ADMIN

}
