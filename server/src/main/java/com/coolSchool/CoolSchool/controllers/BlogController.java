package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.CoolSchool.models.dto.BlogDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.services.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<BlogDTO>> getAllBlogs(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.getAllBlogs((PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.getBlogById(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @PostMapping("/create")
    public ResponseEntity<BlogDTO> createBlog(@Valid @RequestBody BlogDTO blogDTO, HttpServletRequest httpServletRequest) {
        BlogDTO cratedBlog = blogService.createBlog(blogDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedBlog, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDTO> updateBlog(@PathVariable("id") Long id, @Valid @RequestBody BlogDTO blogDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.updateBlog(id, blogDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlogById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        blogService.deleteBlog(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Blog with id: " + id + " has been deleted successfully!");
    }

    @GetMapping("/sort/default")
    public ResponseEntity<List<BlogDTO>> getBlogsByNewest() {
        return ResponseEntity.ok(blogService.getBlogsByNewestFirst());
    }

    @GetMapping("/sort/likes")
    public ResponseEntity<List<BlogDTO>> getBlogsByNumberOfLikes() {
        return ResponseEntity.ok(blogService.getBlogsByMostLiked());
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BlogDTO>> searchBlogsByKeywordTitle(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(blogService.searchBlogsByKeywordTitle(keyword));
    }

    @GetMapping("/search/category")
    public ResponseEntity<List<BlogDTO>> searchBlogsByKeywordSummary(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(blogService.searchBlogsByKeywordCategory(keyword));
    }

    @GetMapping("/search/titleAndCategory")
    public ResponseEntity<List<BlogDTO>> searchBlogsByKeywordInTitleAndCategory(@RequestParam("keywordTitle") String keywordTitle, @RequestParam("keywordCategory") String keywordCategory) {
        return ResponseEntity.ok(blogService.searchBlogsByKeywordInTitleAndCategory(keywordTitle, keywordCategory));
    }
    @GetMapping("/search/titleOrCategory")
    public ResponseEntity<List<BlogDTO>> searchBlogsByKeywordInTitleOrCategory(@RequestParam("keywordTitle") String keywordTitle, @RequestParam("keywordCategory") String keywordCategory) {
        return ResponseEntity.ok(blogService.searchBlogsByKeywordInTitleOrCategory(keywordTitle, keywordCategory));
    }

    @GetMapping("/mostRecent/{n}")
    public ResponseEntity<List<BlogDTO>> getLastNRecentBlogs(@PathVariable("n") int n) {
        return ResponseEntity.ok(blogService.getLastNBlogs(n));
    }
}
