package net.codecraft.jejutrip.board.post.service;

import lombok.RequiredArgsConstructor;
import net.codecraft.jejutrip.account.user.domain.User;
import net.codecraft.jejutrip.account.user.service.UserService;
import net.codecraft.jejutrip.board.attachment.repository.AttachmentRepository;
import net.codecraft.jejutrip.board.attachment.service.AttachmentService;
import net.codecraft.jejutrip.board.post.domain.Post;
import net.codecraft.jejutrip.board.post.dto.PostListResponse;
import net.codecraft.jejutrip.board.post.dto.PostRequest;
import net.codecraft.jejutrip.board.post.dto.PostResponse;
import net.codecraft.jejutrip.board.post.exception.PostException;
import net.codecraft.jejutrip.board.post.repository.PostRepository;
import net.codecraft.jejutrip.common.response.ResponseCode;
import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.common.response.message.PostMessage;
import net.codecraft.jejutrip.security.jwt.support.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public ResponseMessage addPost(PostRequest postRequest , List<MultipartFile> multipartFiles , String token)
            throws IOException {
        User user = userService.findUserByAccessToken(token);
        Post post = createNewPost(postRequest);

        user.addPost(post);
        attachmentService.fileUpload(multipartFiles , post);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public Post createNewPost(PostRequest postRequest) {
        Post post = Post.createPost(postRequest);
        post.addTagFromTagList(postRequest.getTags());

        return post;
    }

    @Transactional
    public ResponseMessage deletePost(long postId , String token) {
        String userId = jwtTokenProvider.getUserPk(token);
        Post post = findPostById(postId);

        isPostOwner(post, userId);
        post.deletePost();

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    private void isPostOwner(Post post, String userId) {
        if(!post.getWriter().getId().equals(userId)) {
            new PostException(PostMessage.ONLY_OWNER_CAN_DELETE);
        }
    }

    @Transactional
    public ResponseMessage increasePostLike(long postId , String token) {
        User user = userService.findUserByAccessToken(token);

        postRepository.findRecommendationFromPost(postId, user.getEmail())
                .ifPresent(a -> { throw new PostException(PostMessage.ALREADY_RECOMMENDED); });
        Post post = findPostById(postId);

        post.addRecommendation(user);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    @Transactional
    public ResponseMessage updatePost(long postId , String token , PostRequest postRequest
            , List<MultipartFile> multipartFiles) throws IOException {
        String userId = jwtTokenProvider.getUserPk(token);

        Post post = findPostByIdAndValidateOwnership(postId , userId);

        post.updatePost(postRequest);
        uploadAttachments(multipartFiles , post);
        deleteAttachments(postRequest.getDeletedFileIds(), postId);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public Post findPostByIdAndValidateOwnership(long postId , String userId) {
        Post post = findPostById(postId);

        if(!post.getWriter().getId().equals(userId)) {
            new PostException(PostMessage.ONLY_OWNER_CAN_MODIFY);
        }

        return post;
    }

    public void deleteAttachments(long[] deletedFileIds, long postId) {
        attachmentService.deletedAttachment(deletedFileIds, postId);
    }

    public void uploadAttachments(List<MultipartFile> multipartFiles, Post post) throws IOException {
        attachmentService.fileUpload(multipartFiles, post);
    }

    public ResponseMessage<List<PostListResponse>> getAllPost(Pageable pageable) {
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS, postRepository.getPostList(pageable));
    }

    public ResponseMessage<PostResponse> findPostByPostId(long postId) {
        Post post = findPostById(postId);

        PostResponse postResponse = PostResponse.createPostResponse(post);
        postResponse.addTagData(postRepository.findTagsInPostId(postId));
        postResponse.setAttachment(attachmentRepository.findAttachmentsByPostId(postId));

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , postResponse);
    }

    @Transactional
    public ResponseMessage updatePostView(long postId) {
        postRepository.updatePostView(postId);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public ResponseMessage<Long> getTotalNumberOfPosts(String type , String data) {
        long result = getTotalNumberAccordingType(type , data);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , result);
    }

    public long getTotalNumberAccordingType(String type , String data) {
        if(type.equals("normal")) {
            return postRepository.getTotalNumberOfPosts();
        } else if(type.equals("tag")) {
            return postRepository.getTotalNumberOfTagSearchPosts(data);
        } else if(type.equals("search")) {
            return postRepository.getTotalNumberOfSearchPosts(data);
        }
        return 0;
    }

    public ResponseMessage findPostBySearch(Pageable pageable, String postContent) {
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , postRepository.findPostBySearch(pageable, postContent));
    }

    public ResponseMessage findPostBySearchAndTag(Pageable pageable, String tag) {
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , postRepository.findPostByTag(pageable, tag));
    }

    public Post findPostById(long postId) {
        Post post = postRepository.findPostByPostId(postId)
                .orElseThrow(() -> new PostException(PostMessage.NOT_FOUNT_POST));

        isDeletedPost(post);

        return post;
    }

    private void isDeletedPost(Post post) {
        if(post.isDelete()) {
            throw new PostException(PostMessage.IS_DELETE_POST);
        }
    }
}