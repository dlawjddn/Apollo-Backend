package com.Teletubbies.Apollo.jwt.domain;

import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name ="authentication")
public class Token {
    @Id
    private String userId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date createdAt;
    private Date updatedAt;
    public Token (String userId, TokenInfo tokenInfo){
        this.userId = userId;
        this.grantType = tokenInfo.getGrantType();
        this.accessToken = tokenInfo.getAccessToken();
        this.refreshToken = tokenInfo.getRefreshToken();
        this.createdAt = tokenInfo.getCreateAt();
        this.updatedAt = tokenInfo.getUpdateAt();
    }

}
