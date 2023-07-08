package com.Teletubbies.Apollo.auth.service;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.core.exception.ApolloException;
import com.Teletubbies.Apollo.core.exception.CustomErrorCode;
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

import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.DUPLICATED_REPO_ERROR;

@Service
@Slf4j
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
                    (Long) jsonDataObject.get("id"),
                    jsonDataObject.get("name").toString(),
                    jsonDataObject.get("html_url").toString())
            );
        }
        return repoInfo;
    }
    @Transactional
    public void saveRepo(User user) throws ParseException {
        List<RepoInfoResponse> repoInfos = getRepoURL(user);
        log.info("get repo list ok");
        for (RepoInfoResponse response : repoInfos) {
            if (repoRepository.existsByOwnerLoginAndRepoName(response.getUserLogin(), response.getRepoName())) continue;
            repoRepository.save(response.changeDTOtoObj(response));
        }
    }
    @Transactional(readOnly = true)
    public List<Repo> findByLogin(User user){
        return repoRepository.findByOwnerLogin(user.getLogin());
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
