package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;

import java.util.List;

public interface BlogService {
    List<BlogResponseDTO> getAllBlogs(PublicUserDTO loggedUser);

    BlogResponseDTO getBlogById(Long id, PublicUserDTO loggedUser);

    BlogResponseDTO createBlog(BlogRequestDTO blogDTO, PublicUserDTO loggedUser);

    BlogResponseDTO updateBlog(Long id, BlogRequestDTO blogDTO, PublicUserDTO loggedUser);

    void deleteBlog(Long id, PublicUserDTO loggedUser);

    List<BlogResponseDTO> getBlogsByNewestFirst();

    List<BlogResponseDTO> getBlogsByMostLiked();

    List<BlogResponseDTO> searchBlogsByKeywordTitle(String keyword);

    List<BlogResponseDTO> searchBlogsByKeywordCategory(String keyword);

    List<BlogResponseDTO> searchBlogsByKeywordInTitleAndCategory(String keywordTitle, String keywordForCategory);

    List<BlogResponseDTO> getLastNBlogs(int n);

    BlogResponseDTO addLike(Long blogId, PublicUserDTO loggedUser);
}
