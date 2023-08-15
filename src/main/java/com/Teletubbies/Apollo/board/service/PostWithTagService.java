package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.repository.PostWithTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostWithTagService {
    private final PostWithTagRepository postWithTagRepository;
    private final TagService tagService;
    @Transactional
    public Long savePostWithTag(Post post, Tag tag){
        return postWithTagRepository.save(new PostWithTag(post, tag)).getId();
    }
    public List<PostWithTag> findPostWithTagByPost(Post post){
        return postWithTagRepository.findAllByPost(post);
    }
    public List<Tag> findSaveTagInUpdate(List<String> originTagNames, List<String> newTagNames){
        List<Tag> saveTags = new ArrayList<>();
        for (String newTagName : newTagNames) {
            int count = 0;
            for (String originTagName : originTagNames) {
                if (originTagName.equals(newTagName)) break; //  keep && new
                count++;
            }
            if (count == originTagNames.size()) {
                if (tagService.existsByTagName(newTagName))
                    saveTags.add(tagService.findByTagName(newTagName));
                else
                    saveTags.add(tagService.saveTag(newTagName));
                //saveTags.add(new Tag(newTagName));
            }
        }
        return saveTags;
    }
    public List<Tag> findDeleteTagInUpdate(List<String> originTagNames, List<String> newTagNames){
        List<Tag> deleteTags = new ArrayList<>();
        for (String originTagName : originTagNames) { // keepTagNames, deleteTagNames 생성완료
            int count = 0;
            for (String newTagName : newTagNames) {
                if (!originTagName.equals(newTagName)) count++;
                else break;
            }
            if (count == newTagNames.size()) {
                deleteTags.add(tagService.findByTagName(originTagName));
            }
        }
        return deleteTags;
    }
    public void updatingPostWithNewTag(Post post, List<Tag> saveTags){
        saveTags.forEach(saveTag -> savePostWithTag(post, saveTag));
    }
    public void updatingPostWithOldTag(Post post, List<Tag> deleteTags){
        deleteTags.forEach(deleteTag -> {
            PostWithTag findPostAndTag = postWithTagRepository.findByPostAndTag(post, deleteTag)
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 게시글 태그 관계입니다."));
            deletePostWithTag(findPostAndTag);
        });
    }
    @Transactional
    public void deletePostWithTag(PostWithTag postWithTag){
        postWithTagRepository.delete(postWithTag);
    }

}
