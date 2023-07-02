package com.Teletubbies.Apollo.auth.service;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
public class RepoService {
    private final RepoRepository repoRepository;
    private static String REPO_LIST_URL = "https://api.github.com/users";
    private static RestTemplate restTemplate = new RestTemplate();
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
                    user.getLogin(),
                    jsonDataObject.get("name").toString(),
                    jsonDataObject.get("html_url").toString())
            );
        }
        return repoInfo;
    }
    public void saveRepo(User user) throws ParseException {
        List<RepoInfoResponse> repoInfos = getRepoURL(user);
        for (RepoInfoResponse response : repoInfos) {
            repoRepository.save(response.changeDTOtoObj(response));
        }
    }
}
