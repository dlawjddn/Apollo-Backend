package com.Teletubbies.Apollo.jwt.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ApolloUserToken{
    @Id
    @Column(name = "user_for_token_id", updatable = false, unique = true, nullable = false)
    private String userLogin;
    @Column(nullable = false)
    private String userId;
    private boolean activated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_for_token_id", referencedColumnName = "user_for_token_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
    public ApolloUserToken(String userLogin, String userId){
        this.userLogin = userLogin;
        this.userId = userId;
        this.activated = true;
    }
}
