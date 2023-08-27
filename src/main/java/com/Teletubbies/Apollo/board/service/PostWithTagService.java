package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.dto.post.response.PostNoContentResponse;
import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import com.Teletubbies.Apollo.board.repository.PostWithTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Long countAssociationByTag(Tag tag){
        return postWithTagRepository.countByTag(tag);
    }
    public List<PostWithTag> findPostWithTagByPost(Post post){
        return postWithTagRepository.findAllByPost(post);
    }
    public List<PostWithTag> findPostWithTagByTag(Tag tag){return postWithTagRepository.findAllByTag(tag);}
    public List<PostNoContentResponse> findPagingPostWithTagByTag(Tag tag, PageRequest pageRequest){
        return postWithTagRepository.findPostWithTagByTag(tag, pageRequest).stream()
                .map(postWithTag -> new PostNoContentResponse(
                        postWithTag.getPost().getApolloUser().getId(),
                        postWithTag.getPost().getId(),
                        postWithTag.getPost().getTitle(),
                        findPostWithTagByPost(postWithTag.getPost()).stream()
                                .map(find -> new ConvertTag(find.getTag().getId(), find.getTag().getName()))
                                .toList(),
                        postWithTag.getPost().getCreateAt()))
                .toList();
    }

    /**
     * post와 tag의 연관관계 업데이트를 위한 함수들
     * private으로 설정한 이유는 update 로직에서만 쓰이고, 이를 하나로 묶어서 판단하기 편하게 하기 위함
     */
    private List<Tag> findSaveTagInUpdate(List<String> originTagNames, List<String> newTagNames){
        List<Tag> saveTags = new ArrayList<>();
        for (String newTagName : newTagNames) {
            int count = 0;
            for (String originTagName : originTagNames) {
                if (originTagName.equals(newTagName)) break; //  기존에 존재하는 이름인 경우
                count++;
            }
            if (count == originTagNames.size()) { // 새로운 태그가 기존 태그와 일치 되는 것이 없음
                if (tagService.existsByTagName(newTagName)) {
                    log.info(newTagName);
                    saveTags.add(tagService.findByTagName(newTagName));
                }
                else
                    saveTags.add(tagService.saveTag(newTagName));
                //saveTags.add(new Tag(newTagName));
            }
        }
        return saveTags;
    }
    private List<Tag> findDeleteTagInUpdate(List<String> originTagNames, List<String> newTagNames){
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
    private void updatingPostWithNewTag(Post post, List<Tag> saveTags){
        saveTags.forEach(saveTag -> savePostWithTag(post, saveTag));
    }
    private void updatingPostWithOldTag(Post post, List<Tag> deleteTags){
        deleteTags.forEach(deleteTag -> {
            PostWithTag findPostAndTag = postWithTagRepository.findByPostAndTag(post, deleteTag)
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 게시글 태그 관계입니다."));
            deletePostWithTag(findPostAndTag);
        });
    }

    /**
     * 실제 업데이트가 이뤄지는 함수
     * 새로 저장, 삭제해야하는 Tag 구분한 뒤, 필요한 로직들 구현
     * @param updatePost
     * @param originTagNames
     * @param newTagNames
     */
    @Transactional
    public void updateAssociationPostAndTag(Post updatePost, List<String> originTagNames, List<String> newTagNames){
        List<Tag> saveTagsAtPost = findSaveTagInUpdate(originTagNames, newTagNames);
        log.info("게시글에 새로 저장해야할 태그 목록 조회 완료");
        List<Tag> deleteTagsAtPost = findDeleteTagInUpdate(originTagNames, newTagNames);
        log.info("게시글에서 삭제해야할 태그 목록 조회 완료");
        updatingPostWithNewTag(updatePost, saveTagsAtPost);
        log.info("새로 저장해야할 게시글 & 태그 연관관계 저장 성공");
        updatingPostWithOldTag(updatePost, deleteTagsAtPost);
        log.info("삭제해야할 게시글 & 태그 연관관계 삭제 성공");
        deleteTagsAtPost.stream()
                .forEach(deleteTagAtPost -> {
                    if (countAssociationByTag(deleteTagAtPost) == 0L) // 어떤 태그에 대해서 게시글과 연관관계가 없으면 size = 0
                        tagService.deleteTag(deleteTagAtPost);
                });
        log.info("게시글과 연관관계가 전혀 없는 태그 삭제 완료");
    }
    @Transactional
    public void deletePostWithTag(PostWithTag postWithTag){
        postWithTagRepository.delete(postWithTag);
    }

}
