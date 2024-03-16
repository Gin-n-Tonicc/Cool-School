package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.blog.BlogNotEnabledException;
import com.coolSchool.coolSchool.exceptions.blog.BlogNotFoundException;
import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.BlogRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.BlogResponseDTO;
import com.coolSchool.coolSchool.models.entity.Blog;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.BlogRepository;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.impl.BlogServiceImpl;
import com.coolSchool.coolSchool.slack.SlackNotifier;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BlogServiceImplTest {
    PublicUserDTO publicUserDTO;
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
    @Mock
    private MessageSource messageSource;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private SlackNotifier slackNotifier;
    @Mock
    private FrontendConfig frontendConfig;

    @BeforeEach
    void setUp() {
        publicUserDTO = new PublicUserDTO(1L, "user", "user", "user@gmail.com", Role.USER, "description", false);
        modelMapper = new ModelMapper();
        frontendConfig = new FrontendConfig();
        frontendConfig.setBaseUrl("http://example.com");
        blogRepository = mock(BlogRepository.class);
        modelMapper = new ModelMapper();
        fileRepository = mock(FileRepository.class);
        userRepository = mock(UserRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        messageSource = mock(MessageSource.class);
        emailSender = new JavaMailSender() {
            @Override
            public MimeMessage createMimeMessage() {
                return null;
            }

            @Override
            public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
                return null;
            }

            @Override
            public void send(MimeMessage mimeMessage) throws MailException {

            }

            @Override
            public void send(MimeMessage... mimeMessages) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {

            }

            @Override
            public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {

            }

            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {

            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {

            }
        };
        FrontendConfig frontendConfig = mock(FrontendConfig.class);
        blogService = new BlogServiceImpl(blogRepository, modelMapper, fileRepository, userRepository, categoryRepository, messageSource, emailSender, slackNotifier, frontendConfig);
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
        assertThrows(BlogNotFoundException.class, () -> blogService.getBlogById(1L, null));
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

    @Test
    void testDeleteBlogNotFound() {
        Long nonExistentBlogId = 99L;

        when(blogRepository.findByIdAndDeletedFalseIsEnabledTrue(nonExistentBlogId)).thenReturn(Optional.empty());

        assertThrows(BlogNotFoundException.class, () -> blogService.deleteBlog(nonExistentBlogId, publicUserDTO));
    }

    @Test
    public void testDeleteBlog_BlogPresent() {
        Long blogId = 1L;

        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);

        Blog blog = new Blog();
        blog.setDeleted(false);
        blog.setOwnerId(user);

        Optional<Blog> blogOptional = Optional.of(blog);
        when(blogRepository.findById(blogId)).thenReturn(blogOptional);
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        assertDoesNotThrow(() -> blogService.deleteBlog(blogId, publicUserDTO));
        assertTrue(blog.isDeleted());
        verify(blogRepository, times(1)).save(blog);
    }

    @Test
    void testGetBlogById_BlogNotFound() {
        Long blogId = 1L;

        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        assertThrows(BlogNotFoundException.class, () -> blogService.getBlogById(blogId, null));
    }

    @Test
    void testGetBlogById_NotEnabled() {
        // Mock data
        Long blogId = 1L;
        Blog mockBlog = new Blog();
        mockBlog.setId(blogId);
        mockBlog.setEnabled(false);

        // Mock repository behavior
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(mockBlog));

        // Assert that BlogNotEnabledException is thrown
        assertThrows(BlogNotEnabledException.class, () -> blogService.getBlogById(blogId, null));
    }

    @Test
    void testGetBlogById_NotFound() {
        // Mock data
        Long blogId = 1L;

        // Mock repository behavior
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // Assert that BlogNotFoundException is thrown
        assertThrows(BlogNotFoundException.class, () -> blogService.getBlogById(blogId, null));
    }
}