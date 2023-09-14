package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.domain.ApolloUser;
import com.Teletubbies.Apollo.domain.Repo;
import com.Teletubbies.Apollo.dto.auth.RepoInfoJsonResponse;
import com.Teletubbies.Apollo.dto.auth.RepoInfoResponse;
import com.Teletubbies.Apollo.repository.RepoRepository;
import com.Teletubbies.Apollo.exception.ApolloException;
import com.Teletubbies.Apollo.exception.CustomErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepoService {
    private final RepoRepository repoRepository;
    private final static String REPO_LIST_URL = "https://api.github.com/users";
    private final static RestTemplate restTemplate = new RestTemplate();
    public List<RepoInfoResponse> getRepoURL(ApolloUser apolloUser) throws ParseException {
        // resource server에 레포 정보 http 요청
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> jsonData = restTemplate.exchange(
                REPO_LIST_URL + "/" + apolloUser.getLogin() + "/repos?per_page=100",
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
                    apolloUser.getLogin(),
                    (Long) jsonDataObject.get("id"),
                    jsonDataObject.get("name").toString(),
                    jsonDataObject.get("html_url").toString(),
                    apolloUser)
            );
        }
        return repoInfo;
    }
    @Transactional
    public List<RepoInfoJsonResponse> saveRepo(ApolloUser apolloUser) throws ParseException {
        List<RepoInfoJsonResponse> repoInfoJson= new ArrayList<>(); // JSON 용 리스트 생성

        List<RepoInfoResponse> repoInfos = getRepoURL(apolloUser);
        log.info("get repo list ok");

        for (RepoInfoResponse repoInfo : repoInfos) {
            repoInfoJson.add(repoInfo.changeObjToJSON(repoInfo));
        }
        log.info("클라이언트에 보낼 리스트 생성 완료");

        for (RepoInfoResponse response : repoInfos) {
            if (repoRepository.existsById(response.getId())) continue;
            repoRepository.save(response.changeDTOtoObj(response));
        }
        log.info("레포지토리 중복 검사 완료");

        return repoInfoJson; //  클라이언트에 보낼 리스트 반환
    }
    @Transactional(readOnly = true)
    public List<Repo> findByLogin(ApolloUser apolloUser){
        return repoRepository.findByOwnerLogin(apolloUser.getLogin());
    }
    @Transactional(readOnly = true)
    public Repo findByName(String repoName){
        Optional<Repo> findRepo = repoRepository.findByRepoName(repoName);
        if (findRepo != null && findRepo.isPresent()) return findRepo.get();
        else throw new ApolloException(CustomErrorCode.NOT_FOUND_REPO_ERROR, "해당 이름에 맞는 레포지토리가 없습니다");
    }
    @Transactional(readOnly = true)
    public Repo findByRepoUrl(String repoUrl){
        Optional<Repo> findRepo = repoRepository.findByRepoUrl(repoUrl);
        if (findRepo != null && findRepo.isPresent()) return findRepo.get();
        else throw new ApolloException(CustomErrorCode.NOT_FOUND_REPO_ERROR, "해당 URL에 맞는 레포지토리가 없습니다");
    }
}
