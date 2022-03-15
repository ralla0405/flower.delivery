package com.kirinit.service.flower.delivery.entity;

import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"name", "username", "role"})
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole role; // ROLE_USER, ROLE_ADMIN

    //===비즈니스 로직===//
    /**
     * 데이터 변경
     */
    public void change(String username, String name, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }
}
