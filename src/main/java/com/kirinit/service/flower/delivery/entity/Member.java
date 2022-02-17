package com.kirinit.service.flower.delivery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kirinit.service.flower.delivery.entity.audit.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", length = 30, unique = true)
    private String username;

    @Column(name = "name", length = 100)
    private String name;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "member_authority",
//            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
//    )
//    private Set<Authority> authorities;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private MemberRole role; // ROLE_USER, ROLE_ADMIN
}
