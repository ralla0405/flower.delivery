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

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "user_id", length = 30, unique = true)
    private String userId;

    @Column(name = "username", length = 100)
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100)
    private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "member_authority",
            joinColumns = {@JoinColumn(name = "member_id", referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

}
