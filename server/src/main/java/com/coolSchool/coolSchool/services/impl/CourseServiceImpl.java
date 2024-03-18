package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.config.FrontendConfig;
import com.coolSchool.coolSchool.enums.Role;
import com.coolSchool.coolSchool.exceptions.category.CategoryNotFoundException;
import com.coolSchool.coolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.Category;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.repositories.CategoryRepository;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.CourseService;
import com.coolSchool.coolSchool.services.UserCourseService;
import com.coolSchool.coolSchool.slack.SlackNotifier;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final UserCourseService userCourseService;
    private final MessageSource messageSource;
    private final SlackNotifier slackNotifier;
    private final FileRepository fileRepository;
    private final FrontendConfig frontendConfig;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper modelMapper, UserRepository userRepository, CategoryRepository categoryRepository, UserCourseService userCourseService, MessageSource messageSource, SlackNotifier slackNotifier, FileRepository fileRepository, FrontendConfig frontendConfig) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userCourseService = userCourseService;
        this.messageSource = messageSource;
        this.slackNotifier = slackNotifier;
        this.fileRepository = fileRepository;
        this.frontendConfig = frontendConfig;
    }

    @Override
    public List<CourseResponseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findByDeletedFalseOrderByCreatedDateDesc();
        return courses.stream().map(course -> modelMapper.map(course, CourseResponseDTO.class)).toList();
    }

    @Override
    public CourseResponseDTO getCourseById(Long id) {
        Optional<Course> course = courseRepository.findByIdAndDeletedFalse(id);
        if (course.isPresent()) {
            return modelMapper.map(course.get(), CourseResponseDTO.class);
        }
        throw new CourseNotFoundException(messageSource);
    }

    @Override
    public boolean canEnrollCourse(Long id, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            return false;
        }

        List<UserCourseResponseDTO> allUserCourses = userCourseService.getAllUserCourses();
        Optional<UserCourseResponseDTO> userCourse = allUserCourses.stream().filter(x -> Objects.equals(x.getCourseId().getId(), id) && Objects.equals(x.getUserId(), loggedUser.getId())).findAny();

        // isEmpty() = true -> Can enroll
        // isEmpty() = false -> Can not enroll (already enrolled)
        return userCourse.isEmpty();
    }

    @Override
    public void enrollCourse(Long id, PublicUserDTO loggedUser) {
        if (loggedUser == null) {
            return;
        }
        // Create a relation between user and course
        UserCourseRequestDTO dto = new UserCourseRequestDTO();
        dto.setCourseId(id);
        dto.setUserId(loggedUser.getId());

        userCourseService.createUserCourse(dto);
    }

    @Override
    public CourseResponseDTO createCourse(CourseRequestDTO courseDTO, PublicUserDTO loggedUser) {
        // Checks if the user is logged in and has the necessary role to access this operation
        if (loggedUser == null || !(loggedUser.getRole().equals(Role.ADMIN) || loggedUser.getRole().equals(Role.TEACHER))) {
            throw new AccessDeniedException(messageSource);
        }

        courseDTO.setId(null);
        courseDTO.setStars(0);
        userRepository.findByIdAndDeletedFalse(courseDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        categoryRepository.findByIdAndDeletedFalse(courseDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(messageSource));
        Course courseEntity = courseRepository.save(modelMapper.map(courseDTO, Course.class));

        // Send a Slack notification to the ADMIN when a new course is created
        sendSlackNotification(courseEntity);
        return modelMapper.map(courseEntity, CourseResponseDTO.class);
    }

    @Override
    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO courseDTO, PublicUserDTO loggedUser) {
        Optional<Course> existingCourseOptional = courseRepository.findByIdAndDeletedFalse(id);

        if (existingCourseOptional.isEmpty()) {
            throw new CourseNotFoundException(messageSource);
        }
        // Checks if the user is logged in and has the necessary role to access this operation
        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), courseDTO.getUserId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException(messageSource);
        }
        userRepository.findByIdAndDeletedFalse(courseDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        categoryRepository.findByIdAndDeletedFalse(courseDTO.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(messageSource));
        Course existingCourse = existingCourseOptional.get();
        modelMapper.map(courseDTO, existingCourse);

        existingCourse.setId(id);
        Course updatedCourse = courseRepository.save(existingCourse);
        return modelMapper.map(updatedCourse, CourseResponseDTO.class);
    }

    @Override
    public void deleteCourse(Long id, PublicUserDTO loggedUser) {
        Optional<Course> course = courseRepository.findByIdAndDeletedFalse(id);
        if (course.isPresent()) {
            // Checks if the user is logged in and has the necessary role to access this operation
            if (loggedUser == null || (!Objects.equals(loggedUser.getId(), course.get().getUser().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
                throw new AccessDeniedException(messageSource);
            }
            course.get().setDeleted(true);
            courseRepository.save(course.get());
        }
        throw new CourseNotFoundException(messageSource);
    }

    public void sendSlackNotification(Course course) {
        // Send a Slack notification to the ADMIN when a new course is created
        User author = userRepository.findById(course.getUser().getId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        Category category = categoryRepository.findById(course.getCategory().getId()).orElseThrow(() -> new CategoryNotFoundException(messageSource));

        String message = "New course created in Cool School:\n" +
                "Name: " + course.getName() + "\n" +
                "Author: " + author.getFirstname() + " " + author.getLastname() + "\n" +
                "Category: " + category.getName() + "\n" +
                "Read more: " + frontendConfig.getBaseUrl() + "/courses/" + course.getId();
        slackNotifier.sendNotification(message);
    }
}
