package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.services.AIAssistanceService;
import com.coolSchool.coolSchool.services.BlogService;
import com.coolSchool.coolSchool.services.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// A controller class for handling blog-related operations.
@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final AIAssistanceService aiAssistanceService;
    private final MessageSource messageSource;

    public BlogController(BlogService blogService, CategoryService categoryService, AIAssistanceService aiAssistanceService, MessageSource messageSource) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.aiAssistanceService = aiAssistanceService;
        this.messageSource = messageSource;
    }


    @GetMapping("/all")
    public ResponseEntity<List<BlogResponseDTO>> getAllBlogs(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.getAllBlogs((PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @RateLimited
    @PostMapping("/like/{blogId}")
    public ResponseEntity<BlogResponseDTO> likeBlog(@PathVariable Long blogId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.addLike(blogId, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> getBlogById(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.getBlogById(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<BlogResponseDTO> createBlog(@Valid @RequestBody BlogRequestDTO blogDTO, HttpServletRequest httpServletRequest) {
        BlogResponseDTO cratedBlog = blogService.createBlog(blogDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedBlog, HttpStatus.CREATED);
    }

    // Generate AI text for blog content. Based on given request
    @RateLimited
    @PostMapping("/generate/AI/text")
    public ResponseEntity<String> generateAIBlogContent(@RequestBody Map<String, String> requestBody) {
        String content = requestBody.get("content");
        String aiGeneratedContent = aiAssistanceService.generateText(content);
        String extractedContent = aiAssistanceService.extractContent(aiGeneratedContent);
        return ResponseEntity.ok(extractedContent);
    }

    // Uses AI assistance to analyze the content and suggest a category for the blog.
    @RateLimited
    @PostMapping("/recommend-category/AI")
    public ResponseEntity<CategoryDTO> recommendCategoryForBlog(@RequestBody Map<String, String> requestBody) throws JsonProcessingException {
        String content = requestBody.get("blogContent");
        List<CategoryDTO> allCategories = categoryService.getAllCategories();

        String prompt = aiAssistanceService.buildPrompt(content, allCategories);
        String aiResponse = aiAssistanceService.analyzeContent(prompt);
        CategoryDTO recommendedCategory = aiAssistanceService.matchCategory(aiResponse, allCategories);

        return ResponseEntity.ok(recommendedCategory);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<BlogResponseDTO> updateBlog(@PathVariable("id") Long id, @Valid @RequestBody BlogRequestDTO blogDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(blogService.updateBlog(id, blogDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlogById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        blogService.deleteBlog(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Blog with id: " + id + " has been deleted successfully!");
    }

    // Retrieves blogs sorted by default (newest first).
    @GetMapping("/sort/default")
    public ResponseEntity<List<BlogResponseDTO>> getBlogsByNewest() {
        return ResponseEntity.ok(blogService.getBlogsByNewestFirst());
    }

    // Retrieve blogs sorted by the number of likes.
    @GetMapping("/sort/likes")
    public ResponseEntity<List<BlogResponseDTO>> getBlogsByNumberOfLikes() {
        return ResponseEntity.ok(blogService.getBlogsByMostLiked());
    }

    // Searches for blogs based on title, category, or both, with an optional sorting parameter.
    @GetMapping("/search/all")
    public ResponseEntity<List<BlogResponseDTO>> searchBlogs(@RequestParam("title") Optional<String> title, @RequestParam("category") Optional<String> category,
                                                             @RequestParam(name = "sort", required = false, defaultValue = "newest") String sort, HttpServletRequest httpServletRequest) {
        List<BlogResponseDTO> result;
        if (title.isPresent() && category.isPresent()) {
            result = blogService.searchBlogsByKeywordInTitleAndCategory(title.get(), category.get());
        } else if (title.isPresent()) {
            result = blogService.searchBlogsByKeywordTitle(title.get());
        } else if (category.isPresent()) {
            result = blogService.searchBlogsByKeywordCategory(category.get());
        } else {
            result = blogService.getAllBlogs((PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        }

        List<BlogResponseDTO> mutableResult = new ArrayList<>(result);
        if ("newest".equals(sort)) {
            mutableResult.sort(Comparator.comparing(BlogResponseDTO::getCreated_at).reversed());
            return ResponseEntity.ok(mutableResult);
        }

        if ("mostLiked".equals(sort)) {
            Comparator<BlogResponseDTO> sortByMostLiked = Comparator.comparingInt(blog -> blog.getLiked_users().size());
            mutableResult.sort(sortByMostLiked.reversed());
            return ResponseEntity.ok(mutableResult);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/mostRecent/{n}") // Retrieves the most recent n blogs.
    public ResponseEntity<List<BlogResponseDTO>> getLastNRecentBlogs(@PathVariable("n") int n) {
        return ResponseEntity.ok(blogService.getLastNBlogs(n));
    }
}
