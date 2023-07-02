package com.Teletubbies.Apollo.auth.service;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.AccessTokenRequest;
import com.Teletubbies.Apollo.auth.dto.AccessTokenResponse;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String MEMBER_INFO_URL = "https://api.github.com/user";
    private static final String REPO_LIST_URL = "https://api.github.com/users";
    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${security.oauth.github.client-id}")
    private String clientId;
    @Value("${security.oauth.github.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        return restTemplate.postForObject(
                ACCESS_TOKEN_URL,
                new AccessTokenRequest(clientId, clientSecret, code),
                AccessTokenResponse.class
        ).getAccessToken();
    }

    public ResponseEntity<MemberInfoResponse> getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        return restTemplate.exchange(
                MEMBER_INFO_URL,
                HttpMethod.GET,
                request,
                MemberInfoResponse.class
        );
    }
    public List<RepoInfoResponse> getRepoURL(User user) throws ParseException {
        // resource server에 레포 정보 http 요청
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> jsonData = restTemplate.exchange(
                REPO_LIST_URL + "/" + user.getLogin() + "/repos",
                HttpMethod.GET,
                request,
                String.class
        );
        // json 정보를 바탕으로 parsing
        List<RepoInfoResponse> repoInfo = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonData.getBody());

        // json에서 추출한 정보를 바탕으로 dto list 생성
        for (int i=0; i< jsonArray.size(); i++){
            JSONObject jsonDataObject = (JSONObject) jsonArray.get(i);
            repoInfo.add(new RepoInfoResponse(
                    jsonDataObject.get("name").toString(),
                    jsonDataObject.get("html_url").toString())
            );
        }
        return repoInfo;
    }
}
