package net.codecraft.jejutrip.board.post.controller;

import net.codecraft.jejutrip.common.response.ResponseMessage;
import net.codecraft.jejutrip.board.post.dto.PostRequest;
import net.codecraft.jejutrip.board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.login.AccountException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/post", consumes = {MediaType.APPLICATION_JSON_VALUE , MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseMessage> addPost(@RequestPart PostRequest postRequest
            , @RequestPart(required = false) List<MultipartFile> multipartFiles , @CookieValue String accessToken)
            throws IOException {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.addPost(postRequest , multipartFiles , accessToken));
    }

    @GetMapping(value = "/posts/count")
    public ResponseEntity<ResponseMessage> getTotalNumberOfPosts(@RequestParam String type , @RequestParam(required = false) String data) {
        return ResponseEntity.ok().body(postService.getTotalNumberOfPosts(type , data));
    }

    @GetMapping(value = "/posts")
    public ResponseEntity<ResponseMessage> getAllPost(Pageable pageable) {
        return ResponseEntity.ok().body(postService.getAllPost(pageable));
    }

    @GetMapping(value = "/posts/{postId}")
    public ResponseEntity<ResponseMessage> selectOnePost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok().body(postService.findPostByPostId(postId));
    }

    @DeleteMapping(value = "/posts/{postId}")
    public ResponseEntity<ResponseMessage> deletePost(@PathVariable("postId") Long postId , @CookieValue String accessToken) {
        return ResponseEntity.ok().body(postService.deletePost(postId , accessToken));
    }

    @PutMapping(value = "/posts/{postId}")
    public ResponseEntity<ResponseMessage> updatePost(@PathVariable("postId") Long postId
            , @CookieValue String accessToken , @RequestPart PostRequest postRequest
            , @RequestPart(required = false) List<MultipartFile> multipartFiles) throws IOException {

        return ResponseEntity.ok().body(postService.updatePost(postId, accessToken, postRequest , multipartFiles));
    }

    @PatchMapping(value = "/posts/views/{postId}")
    public ResponseEntity<ResponseMessage> increasePostViews(@PathVariable long postId) {
        return ResponseEntity.ok().body(postService.updatePostView(postId));
    }

    @PatchMapping(value = "/posts/likes/{postId}")
    public ResponseEntity<ResponseMessage> increasePostLike(@PathVariable Long postId , @CookieValue String accessToken) throws AccountException {
        return ResponseEntity.ok().body(postService.increasePostLike(postId, accessToken));
    }

    @GetMapping(value = "/posts/search/{postContent}")
    public ResponseEntity<ResponseMessage> findPostBySearch(Pageable pageable, @PathVariable String postContent) {
        return ResponseEntity.ok().body(postService.findPostBySearch(pageable, postContent));
    }

    @GetMapping(value = "/posts/search/tags/{tagData}")
    public ResponseEntity<ResponseMessage> findPostByTag(Pageable pageable, @PathVariable String tagData) {
        return ResponseEntity.ok().body(postService.findPostBySearchAndTag(pageable, tagData));
    }
}