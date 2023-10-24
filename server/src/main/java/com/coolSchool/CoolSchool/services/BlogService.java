package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.BlogDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;

import java.util.List;

public interface BlogService {
    List<BlogDTO> getAllBlogs(PublicUserDTO loggedUser);

    BlogDTO getBlogById(Long id, PublicUserDTO loggedUser);

    BlogDTO createBlog(BlogDTO blogDTO, PublicUserDTO loggedUser);

    BlogDTO updateBlog(Long id, BlogDTO blogDTO, PublicUserDTO loggedUser);

    void deleteBlog(Long id, PublicUserDTO loggedUser);

    List<BlogDTO> getBlogsByNewestFirst();

    List<BlogDTO> getBlogsByMostLiked();

    List<BlogDTO> searchBlogsByKeywordTitle(String keyword);

    List<BlogDTO> searchBlogsByKeywordCategory(String keyword);

    List<BlogDTO> searchBlogsByKeywordInTitleAndCategory(String keywordTitle, String keywordForCategory);
    List<BlogDTO> searchBlogsByKeywordInTitleOrCategory(String keywordTitle, String keywordForCategory);
    List<BlogDTO> getLastNBlogs(int n);
}
