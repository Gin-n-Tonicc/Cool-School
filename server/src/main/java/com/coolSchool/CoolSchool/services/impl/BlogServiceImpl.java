package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.CoolSchool.exceptions.blog.ValidationBlogException;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.common.BadRequestException;
import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.models.dto.BlogDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Blog;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.BlogRepository;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.BlogService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    public BlogServiceImpl(BlogRepository blogRepository, ModelMapper modelMapper, FileRepository fileRepository, UserRepository userRepository, Validator validator) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    public List<BlogDTO> getAllBlogs(PublicUserDTO loggedUser) {
        if(loggedUser != null) {
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                List<Blog> blogs = blogRepository.findAll();
                return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
            }
        }
        List<Blog> blogs = blogRepository.findByDeletedFalseAndIsEnabledTrue();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }

    @Override
    public BlogDTO getBlogById(Long id, PublicUserDTO loggedUser) {
        if(loggedUser != null) {
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                Optional<Blog> blog = blogRepository.findById(id);
                return modelMapper.map(blog.get(), BlogDTO.class);
            }
        }
        Optional<Blog> blog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        if (blog.isPresent()) {
            return modelMapper.map(blog.get(), BlogDTO.class);
        }
        throw new BlogNotFoundException();
    }

    @Override
    public BlogDTO createBlog(BlogDTO blogDTO, PublicUserDTO loggedUser) {
        try {
            blogDTO.setId(null);
            blogDTO.setCreated_at(LocalDateTime.now());
            blogDTO.setOwnerId(loggedUser.getId());
            if(loggedUser.getRole().equals(Role.ADMIN)){
                blogDTO.setIsEnabled(blogDTO.getIsEnabled());
            }
            userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId()).orElseThrow(NoSuchElementException::new);
            Blog blogEntity = blogRepository.save(modelMapper.map(blogDTO, Blog.class));
            return modelMapper.map(blogEntity, BlogDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationBlogException(exception.getConstraintViolations());
        }
    }

    @Override
    public BlogDTO updateBlog(Long id, BlogDTO blogDTO, PublicUserDTO loggedUser) {
        Optional<Blog> existingBlogOptional = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        if (existingBlogOptional.isEmpty()) {
            throw new BlogNotFoundException();
        }
        User user = userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId()).orElseThrow(NoSuchElementException::new);

        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), user.getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException();
        }
        if(loggedUser.getRole().equals(Role.ADMIN)){
            blogDTO.setIsEnabled(blogDTO.getIsEnabled());
        }
        Blog existingBlog = existingBlogOptional.get();
        modelMapper.map(blogDTO, existingBlog);

        try {
            existingBlog.setId(id);
            Blog updatedBlog = blogRepository.save(existingBlog);
            return modelMapper.map(updatedBlog, BlogDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationBlogException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    @Transactional
    public void deleteBlog(Long id, PublicUserDTO loggedUser) {
        Optional<Blog> blogOptional = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();

            if (loggedUser == null || (!Objects.equals(loggedUser.getId(), blog.getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
                throw new AccessDeniedException();
            }
            blog.setDeleted(true);
            blogRepository.save(blog);
        } else {
            throw new BlogNotFoundException();
        }
    }

    @Override
    public List<BlogDTO> getBlogsByNewestFirst() {
        List<Blog> blogs = blogRepository.findAllByNewestFirst();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }

    @Override
    public List<BlogDTO> getBlogsByMostLiked() {
        List<Blog> blogs = blogRepository.findAllByMostLiked();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }

    @Override
    public List<BlogDTO> searchBlogsByKeywordTitle(String keyword) {
        List<Blog> blogs = blogRepository.searchByTitleContainingIgnoreCase(keyword.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }

    @Override
    public List<BlogDTO> searchBlogsByKeywordCategory(String keyword) {
        List<Blog> blogs = blogRepository.findByCategoryIdName(keyword.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }
    @Override
    public List<BlogDTO> searchBlogsByKeywordInTitleAndCategory(String keyword) {
        List<Blog> blogs = blogRepository.searchByTitleAndCategoryName(keyword.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
    }

    @Override
    public List<BlogDTO> getLastNBlogs(int n) {
        if (n >= 0) {
            List<Blog> allBlogs = blogRepository.findByDeletedFalseAndIsEnabledTrue();
            List<Blog> sortedBlogs = allBlogs.stream()
                    .sorted((blog1, blog2) -> Long.compare(blog2.getId(), blog1.getId()))
                    .collect(Collectors.toList());
            List<Blog> lastNBlogs = sortedBlogs.subList(0, Math.min(n, sortedBlogs.size()));
            return lastNBlogs.stream().map(blog -> modelMapper.map(blog, BlogDTO.class)).toList();
        }
        throw new BadRequestException();
    }
}
