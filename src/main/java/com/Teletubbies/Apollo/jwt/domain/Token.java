package com.Teletubbies.Apollo.jwt.domain;

import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
public class Token {
    @Id
    private String userId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date createAt;
    private Date updateAt;
    public Token (String userId, TokenInfo tokenInfo){
        this.userId = userId;
        this.grantType = tokenInfo.getGrantType();
        this.accessToken = tokenInfo.getAccessToken();
        this.refreshToken = tokenInfo.getRefreshToken();
        this.createAt = tokenInfo.getCreateAt();
        this.updateAt = tokenInfo.getUpdateAt();
    }

}
