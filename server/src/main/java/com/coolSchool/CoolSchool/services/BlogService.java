package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.BlogDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;

import java.util.List;

public interface BlogService {
    List<BlogDTO> getAllBlogs();

    BlogDTO getBlogById(Long id);

    BlogDTO createBlog(BlogDTO blogDTO);

    BlogDTO updateBlog(Long id, BlogDTO blogDTO, PublicUserDTO loggedUser);

    void deleteBlog(Long id,  PublicUserDTO loggedUser);

    List<BlogDTO> getBlogsByNewestFirst();

    List<BlogDTO> getBlogsByMostLiked();

    List<BlogDTO> searchBlogsByKeywordTitle(String keyword);

    List<BlogDTO> searchBlogsByKeywordSummary(String keyword);
}
