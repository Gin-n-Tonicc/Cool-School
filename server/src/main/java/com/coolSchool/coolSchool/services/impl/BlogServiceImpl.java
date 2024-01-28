package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.blog.BlogAlreadyLikedException;
import com.coolSchool.coolSchool.exceptions.blog.BlogNotEnabledException;
import com.coolSchool.coolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.coolSchool.exceptions.blog.ValidationBlogException;
import com.coolSchool.coolSchool.exceptions.category.CategoryNotFoundException;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import com.coolSchool.coolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.models.entity.Blog;
import com.coolSchool.coolSchool.models.entity.Category;
import com.coolSchool.coolSchool.models.entity.File;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.BlogService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Validator validator;
    private final MessageSource messageSource;

    public BlogServiceImpl(BlogRepository blogRepository, ModelMapper modelMapper, FileRepository fileRepository, UserRepository userRepository, CategoryRepository categoryRepository, Validator validator, MessageSource messageSource) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
        this.messageSource = messageSource;
    }

    @Override
    public BlogResponseDTO addLike(Long blogId, PublicUserDTO loggedUser) {
        if (loggedUser != null) {
            Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new BlogNotFoundException(messageSource));
            User user = userRepository.findByIdAndDeletedFalse(loggedUser.getId()).orElseThrow(()-> new UserNotFoundException(messageSource));
            if (!blog.getLiked_users().contains(user)) {
                blog.getLiked_users().add(user);
                blog = blogRepository.save(blog);
                return modelMapper.map(blog, BlogResponseDTO.class);
            }
            throw new BlogAlreadyLikedException(messageSource);
        }
        throw new AccessDeniedException(messageSource);
    }

    @Override
    public List<BlogResponseDTO> getAllBlogs(PublicUserDTO loggedUser) {
        if (loggedUser != null) {
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                List<Blog> blogs = blogRepository.findAll();
                return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
            }
        }
        List<Blog> blogs = blogRepository.findByDeletedFalseAndIsEnabledTrue();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override

    public BlogResponseDTO getBlogById(Long id, PublicUserDTO loggedUser) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);

        if (loggedUser != null) {
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                optionalBlog = blogRepository.findById(id);
            }
        }
        if (optionalBlog.isEmpty()) {
            optionalBlog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        }
        if (optionalBlog.isPresent()) {
            if (!(optionalBlog.get().isEnabled())) {
                throw new BlogNotEnabledException(messageSource);
            }
        }
        return modelMapper.map(optionalBlog.get(), BlogResponseDTO.class);
    }

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO blogDTO, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            throw new AccessDeniedException(messageSource);
        }
        try {
            blogDTO.setId(null);
            blogDTO.setCreated_at(LocalDateTime.now());
            blogDTO.setOwnerId(loggedUser.getId());
            blogDTO.setEnabled(loggedUser.getRole().equals(Role.ADMIN));

            userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId()).orElseThrow(()-> new UserNotFoundException(messageSource));
            categoryRepository.findByIdAndDeletedFalse(blogDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(messageSource));
            fileRepository.findByIdAndDeletedFalse(blogDTO.getPictureId()).orElseThrow(()-> new FileNotFoundException(messageSource));

            blogDTO.setCommentCount(0);
            Blog blogEntity = blogRepository.save(modelMapper.map(blogDTO, Blog.class));
            return modelMapper.map(blogEntity, BlogResponseDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationBlogException(exception.getConstraintViolations());
        }
    }

    @Override
    public BlogResponseDTO updateBlog(Long id, BlogRequestDTO blogDTO, PublicUserDTO loggedUser) {
        Optional<Blog> existingBlogOptional = blogRepository.findById(id);
        Category category = categoryRepository.findByIdAndDeletedFalse(blogDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(messageSource));
        File file = fileRepository.findByIdAndDeletedFalse(blogDTO.getPictureId()).orElseThrow(()-> new FileNotFoundException(messageSource));
        User user = userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId()).orElseThrow(()-> new UserNotFoundException(messageSource));
        Set<User> userSet = blogDTO.getLiked_users().stream().map(x -> userRepository.findByIdAndDeletedFalse(x).orElseThrow(()-> new UserNotFoundException(messageSource))).collect(Collectors.toSet());

        if (existingBlogOptional.isEmpty()) {
            throw new BlogNotFoundException(messageSource);
        }

        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), existingBlogOptional.get().getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException(messageSource);
        }
        if (loggedUser.getRole().equals(Role.ADMIN)) {
            blogDTO.setEnabled(blogDTO.isEnabled());
        } else {
            blogDTO.setEnabled(existingBlogOptional.get().isEnabled());
        }

        Blog existingBlog = existingBlogOptional.get();
        blogDTO.setCommentCount(existingBlog.getCommentCount());
        modelMapper.map(blogDTO, existingBlog);

        try {
            existingBlog.setId(id);
            existingBlog.setCreated_at(existingBlog.getCreated_at());

            existingBlog.setPicture(file);
            existingBlog.setCategoryId(category);
            existingBlog.setOwnerId(user);
            existingBlog.setLiked_users(userSet);

            Blog updatedBlog = blogRepository.save(existingBlog);
            return modelMapper.map(updatedBlog, BlogResponseDTO.class);
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
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new BlogNotFoundException(messageSource));

        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), blog.getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException(messageSource);
        }

        blog.setDeleted(true);
        blogRepository.save(blog);
    }

    @Override
    public List<BlogResponseDTO> getBlogsByNewestFirst() {
        List<Blog> blogs = blogRepository.findAllByNewestFirst();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override
    public List<BlogResponseDTO> getBlogsByMostLiked() {
        List<Blog> blogs = blogRepository.findAllByMostLiked();
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override
    public List<BlogResponseDTO> searchBlogsByKeywordTitle(String keyword) {
        List<Blog> blogs = blogRepository.searchByTitleContainingIgnoreCase(keyword.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override
    public List<BlogResponseDTO> searchBlogsByKeywordCategory(String keyword) {
        List<Blog> blogs = blogRepository.findByCategoryIdName(keyword.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override
    public List<BlogResponseDTO> searchBlogsByKeywordInTitleAndCategory(String keywordForTitle, String keywordForCategory) {
        List<Blog> blogs = blogRepository.searchBlogsByKeywordInTitleAndCategory(keywordForTitle.toLowerCase(), keywordForCategory.toLowerCase());
        return blogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
    }

    @Override
    public List<BlogResponseDTO> getLastNBlogs(int n) {
        if (n >= 0) {
            List<Blog> allBlogs = blogRepository.findByDeletedFalseAndIsEnabledTrue();
            List<Blog> sortedBlogs = allBlogs.stream().sorted((blog1, blog2) -> Long.compare(blog2.getId(), blog1.getId())).collect(Collectors.toList());
            List<Blog> lastNBlogs = sortedBlogs.subList(0, Math.min(n, sortedBlogs.size()));
            return lastNBlogs.stream().map(blog -> modelMapper.map(blog, BlogResponseDTO.class)).toList();
        }
        throw new BadRequestException(messageSource);
    }

}