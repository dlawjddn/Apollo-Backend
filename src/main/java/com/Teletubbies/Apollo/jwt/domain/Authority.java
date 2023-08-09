package com.Teletubbies.Apollo.jwt.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id = null;
    @Column(name = "authority_name")
    private String authorityName;

    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
