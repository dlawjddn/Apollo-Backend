package com.Teletubbies.Apollo.domain;

import com.Teletubbies.Apollo.domain.ApolloUserToken;
import com.Teletubbies.Apollo.domain.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "user_authority")
@Getter
public class UserAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_for_token_id")
    private ApolloUserToken apolloUserToken;
    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;

    public UserAuthority(ApolloUserToken apolloUserToken, Authority authority) {
        this.apolloUserToken = apolloUserToken;
        this.authority = authority;
    }
}
