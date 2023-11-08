package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.blog.BlogAlreadyLikedException;
import com.coolSchool.CoolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.CoolSchool.exceptions.blog.ValidationBlogException;
import com.coolSchool.CoolSchool.exceptions.category.CategoryNotFoundException;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.common.BadRequestException;
import com.coolSchool.CoolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.CoolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.CoolSchool.models.entity.Blog;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.BlogRepository;
import com.coolSchool.CoolSchool.repositories.CategoryRepository;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.BlogService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
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
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public BlogServiceImpl(BlogRepository blogRepository, ModelMapper modelMapper, FileRepository fileRepository, UserRepository userRepository, CategoryRepository categoryRepository, Validator validator) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public BlogResponseDTO addLike(Long blogId, PublicUserDTO loggedUser) {
        if (loggedUser != null) {
            Blog blog = blogRepository.findById(blogId).orElseThrow(BlogNotFoundException::new);
            User user = userRepository.findByIdAndDeletedFalse(loggedUser.getId()).orElseThrow(UserNotFoundException::new);
            if (!blog.getLiked_users().contains(user)) {
                blog.getLiked_users().add(user);
                blog = blogRepository.save(blog);
                return modelMapper.map(blog, BlogResponseDTO.class);
            }

            throw new BlogAlreadyLikedException();
        }
        throw new AccessDeniedException();
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
        Optional<Blog> optionalBlog = Optional.empty();

        if (loggedUser != null) {
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                optionalBlog = blogRepository.findById(id);
            }
        }

        if (optionalBlog.isEmpty()) {
            optionalBlog = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        }

        if (optionalBlog.isPresent()) {
            return modelMapper.map(optionalBlog.get(), BlogResponseDTO.class);
        }

        throw new BlogNotFoundException();
    }

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO blogDTO, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            throw new AccessDeniedException();
        }
        try {
            blogDTO.setId(null);
            blogDTO.setCreated_at(LocalDateTime.now());
            blogDTO.setOwnerId(loggedUser.getId());
            if (loggedUser.getRole().equals(Role.ADMIN)) {
                blogDTO.setEnabled(blogDTO.isEnabled());
            } else {
                blogDTO.setEnabled(false);
            }

            userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId()).orElseThrow(UserNotFoundException::new);
            categoryRepository.findByIdAndDeletedFalse(blogDTO.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
            fileRepository.findByIdAndDeletedFalse(blogDTO.getPictureId()).orElseThrow(FileNotFoundException::new);

            blogDTO.setCommentCount(0);
            Blog blogEntity = blogRepository.save(modelMapper.map(blogDTO, Blog.class));
            return modelMapper.map(blogEntity, BlogResponseDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationBlogException(exception.getConstraintViolations());
        }
    }

    @Override
    public BlogResponseDTO updateBlog(Long id, BlogRequestDTO blogDTO, PublicUserDTO loggedUser) {
        Optional<Blog> existingBlogOptional = blogRepository.findByIdAndDeletedFalseIsEnabledTrue(id);
        blogRepository.findByIdAndDeletedFalseIsEnabledTrue(blogDTO.getCategoryId()).orElseThrow(BlogNotFoundException::new);
        fileRepository.findByIdAndDeletedFalse(blogDTO.getPictureId()).orElseThrow(FileNotFoundException::new);
        if (existingBlogOptional.isEmpty()) {
            throw new BlogNotFoundException();
        }

        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), existingBlogOptional.get().getOwnerId().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException();
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
        throw new BadRequestException();
    }

}