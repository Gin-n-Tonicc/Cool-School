package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.models.entity.Blog;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.BlogServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    private MessageSource m

    @BeforeEach
    void setUp() {
        blogRepository = mock(BlogRepository.class);
        modelMapper = new ModelMapper();
        fileRepository = mock(FileRepository.class);
        userRepository = mock(UserRepository.class);
        validator = new LocalValidatorFactoryBean();
        categoryRepository = mock(CategoryRepository.class);
        blogService = new BlogServiceImpl(blogRepository, modelMapper, fileRepository, userRepository, categoryRepository, validator, messageSource);
    }

    @Test
    void testGetAllBlogs() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        when(blogRepository.findAll()).thenReturn(Collections.emptyList());
        List<BlogResponseDTO> blogs = blogService.getAllBlogs(loggedUser);
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(0, blogs.size());
    }

    @Test
    void testGetBlogsByNewestFirstWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findAllByNewestFirst()).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogs = blogService.getBlogsByNewestFirst();
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetBlogsByMostLikedWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findAllByMostLiked()).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogs = blogService.getBlogsByMostLiked();
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testSearchBlogsByKeywordTitleWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.searchByTitleContainingIgnoreCase(anyString())).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogs = blogService.searchBlogsByKeywordTitle("keyword");
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testSearchBlogsByKeywordCategoryWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findByCategoryIdName(anyString())).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogs = blogService.searchBlogsByKeywordCategory("category");
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetLastNBlogsWithResults() {
        List<Blog> mockBlogs = Collections.singletonList(new Blog());
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogs = blogService.getLastNBlogs(5);
        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
    }

    @Test
    void testGetLastNBlogsWithResultsThrowException() {
        assertThrows(BadRequestException.class, () -> blogService.getLastNBlogs(-5));
    }

    @Test
    void testGetBlogByIdNotFound() {
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> blogService.getBlogById(1L, null));
    }

    @Test
    void testGetAllBlogsAsAdmin() {
        List<Blog> mockBlogs = List.of(new Blog(), new Blog());
        when(blogRepository.findAll()).thenReturn(mockBlogs);
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(List.of());
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.ADMIN);
        List<BlogResponseDTO> blogDTOs = blogService.getAllBlogs(loggedUser);
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
        List<BlogResponseDTO> blogDTOs = blogService.getAllBlogs(loggedUser);
        Assertions.assertNotNull(blogDTOs);
        Assertions.assertFalse(blogDTOs.isEmpty());
    }

    @Test
    void testGetAllBlogsAsGuest() {
        List<Blog> mockBlogs = List.of(new Blog(), new Blog());
        when(blogRepository.findAll()).thenReturn(List.of());
        when(blogRepository.findByDeletedFalseAndIsEnabledTrue()).thenReturn(mockBlogs);
        List<BlogResponseDTO> blogDTOs = blogService.getAllBlogs(null);
        Assertions.assertNotNull(blogDTOs);
        Assertions.assertFalse(blogDTOs.isEmpty());
    }

    @Test
    public void testSearchBlogsByKeywordInTitleAndCategory() {
        String keywordForTitle = "programming";
        String keywordForCategory = "tech";
        List<Blog> mockBlogs = List.of(
                new Blog(),
                new Blog(),
                new Blog()
        );

        when(blogRepository.searchBlogsByKeywordInTitleAndCategory(keywordForTitle.toLowerCase(), keywordForCategory.toLowerCase()))
                .thenReturn(mockBlogs);

        List<BlogResponseDTO> result = blogService.searchBlogsByKeywordInTitleAndCategory(keywordForTitle, keywordForCategory);

        List<BlogResponseDTO> expectedDTOs = mockBlogs.stream()
                .map(blog -> new BlogResponseDTO())
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedDTOs, result);
    }

    @Test
    void testCreateBlogWithInvalidUser() {
        PublicUserDTO loggedUser = new PublicUserDTO();
        loggedUser.setRole(Role.USER);
        loggedUser.setId(1L);
        BlogResponseDTO blogDTO = new BlogResponseDTO();
        blogDTO.setEnabled(true);
        blogDTO.setOwner(loggedUser);
        blogDTO.setId(null);
        BlogRequestDTO blogRequestDTO = new BlogRequestDTO();
        blogRequestDTO.setEnabled(true);
        blogRequestDTO.setOwnerId(loggedUser.getId());
        blogRequestDTO.setId(null);
        when(userRepository.findByIdAndDeletedFalse(blogDTO.getOwner().getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> blogService.createBlog(blogRequestDTO, loggedUser));
    }
}