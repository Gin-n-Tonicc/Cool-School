package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.CoolSchool.exceptions.common.BadRequestException;
import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.models.dto.BlogDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Blog;
import com.coolSchool.CoolSchool.models.entity.Category;
import com.coolSchool.CoolSchool.models.entity.File;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.BlogRepository;
import com.coolSchool.CoolSchool.repositories.CategoryRepository;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.BlogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlogServiceImplTest {
    @InjectMocks
    private BlogServiceImpl blogService;
    @Mock
    private BlogRepository blogRepository;
    private ModelMapper modelMapper;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    private LocalValidatorFactoryBean validator;

    @BeforeEach
    void setUp() {
        blogRepository = mock(BlogRepository.class);
        modelMapper = new ModelMapper();
        fileRepository = mock(FileRepository.class);
        userRepository = mock(UserRepository.class);
        validator = new LocalValidatorFactoryBean();
        categoryRepository = mock(CategoryRepository.class);
        blogService = new BlogServiceImpl(blogRepository, modelMapper, fileRepository, userRepository, categoryRepository, validator);
    }

    @Test
    void testGetAllBlogs() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        when(blogRepository.findAll()).thenReturn(Collections.emptyList());
        List<BlogDTO> blogs = blogService.getAllBlogs(loggedUser);
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(0, blogs.size());
    }

    @Test
    void testGetBlogById() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        Blog sampleBlog = new Blog();
        when(blogRepository.findById(anyLong())).thenReturn(Optional.of(sampleBlog));
        BlogDTO blogDTO = blogService.getBlogById(1L, loggedUser);
        Assertions.assertNotNull(blogDTO);
    }

    @Test
    void testDeleteBlog() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setId(1L);
        loggedUser.setRole(Role.ADMIN);
        Blog blog = new Blog();
        blog.setId(1L);
        User user = new User();
        user.setId(2L);
        blog.setOwnerId(user);
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(anyLong())).thenReturn(Optional.of(blog));
        assertDoesNotThrow(() -> blogService.deleteBlog(1L, loggedUser));
    }

    @Test
    void testGetBlogsByNewestFirstWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findAllByNewestFirst()).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.getBlogsByNewestFirst();
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetBlogsByMostLikedWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findAllByMostLiked()).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.getBlogsByMostLiked();
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testSearchBlogsByKeywordTitleWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.searchByTitleContainingIgnoreCase(anyString())).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.searchBlogsByKeywordTitle("keyword");
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testSearchBlogsByKeywordCategoryWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findByCategoryIdName(anyString())).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.searchBlogsByKeywordCategory("category");
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testSearchBlogsByKeywordInTitleAndCategoryWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.searchByTitleAndCategoryName(anyString())).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.searchBlogsByKeywordInTitleAndCategory("keywordCategory");
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetLastNBlogsWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(mockBlogs);
        List<BlogDTO> blogs = blogService.getLastNBlogs(5);
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetLastNBlogsWithResultsThrowException() {
        assertThrows(BadRequestException.class, () -> blogService.getLastNBlogs(-5));
    }

    @Test
    void testGetBlogByIdAsAdmin() {
        Optional<Blog> mockBlog = Optional.of(new Blog());
        when(blogRepository.findById(anyLong())).thenReturn(mockBlog);
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(anyLong())).thenReturn(Optional.empty());
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        BlogDTO blogDTO = blogService.getBlogById(1L, loggedUser);
        Assertions.assertNotNull(blogDTO);
    }

    @Test
    void testGetBlogByIdAsUser() {
        Optional<Blog> mockBlog = Optional.of(new Blog());
        when(blogRepository.findById(anyLong())).thenReturn(mockBlog);
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(anyLong())).thenReturn(Optional.of(new Blog()));
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.USER);
        BlogDTO blogDTO = blogService.getBlogById(1L, loggedUser);
        Assertions.assertNotNull(blogDTO);
    }

    @Test
    void testGetBlogByIdNotFound() {
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(anyLong())).thenReturn(Optional.empty());
        assertThrows(BlogNotFoundException.class, () -> blogService.getBlogById(1L, null));
    }

    @Test
    void testGetAllBlogsAsAdmin() {
        List<Blog> mockBlogs = List.of(new Blog(), new Blog());
        when(blogRepository.findAll()).thenReturn(mockBlogs);
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(List.of());
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        List<BlogDTO> blogDTOs = blogService.getAllBlogs(loggedUser);
        Assertions.assertNotNull(blogDTOs);
        Assertions.assertFalse(blogDTOs.isEmpty());
    }

    @Test
    void testGetAllBlogsAsUser() {
        List<Blog> mockBlogs = List.of(new Blog(), new Blog());
        when(blogRepository.findAll()).thenReturn(List.of());
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(mockBlogs);
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.USER);
        List<BlogDTO> blogDTOs = blogService.getAllBlogs(loggedUser);
        Assertions.assertNotNull(blogDTOs);
        Assertions.assertFalse(blogDTOs.isEmpty());
    }

    @Test
    void testGetAllBlogsAsGuest() {
        List<Blog> mockBlogs = List.of(new Blog(), new Blog());
        when(blogRepository.findAll()).thenReturn(List.of());
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(mockBlogs);
        List<BlogDTO> blogDTOs = blogService.getAllBlogs(null);
        Assertions.assertNotNull(blogDTOs);
        Assertions.assertFalse(blogDTOs.isEmpty());
    }

    @Test
    void testCreateBlogAsAdmin() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        loggedUser.setId(1L);
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setEnabled(true);
        blogDTO.setOwnerId(loggedUser.getId());
        blogDTO.setId(null);
        when(userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(new Category()));
        when(fileRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId())).thenReturn(Optional.of(new File()));
        BlogDTO createdBlogDTO = blogService.createBlog(blogDTO, loggedUser);
        Assertions.assertNotNull(createdBlogDTO);
        Assertions.assertTrue(blogDTO.isEnabled());
    }
    @Test
    void testCreateBlogWithInvalidUser() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.USER);
        loggedUser.setId(1L);
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setEnabled(true);
        blogDTO.setOwnerId(loggedUser.getId());
        blogDTO.setId(null);
        when(userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> blogService.createBlog(blogDTO, loggedUser));
    }

    @Test
    void testUpdateBlogWithInvalidUserThrowsException() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.USER);
        loggedUser.setId(1L);
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setEnabled(true);
        blogDTO.setOwnerId(loggedUser.getId());
        long existingBlogId = 123L;
        when(userRepository.findByIdAndDeletedFalse(blogDTO.getOwnerId())).thenReturn(Optional.empty());
        assertThrows(BlogNotFoundException.class, () -> blogService.updateBlog(existingBlogId, blogDTO, loggedUser));
    }
}
