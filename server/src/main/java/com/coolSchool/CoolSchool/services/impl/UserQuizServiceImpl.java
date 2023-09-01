package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.userQuiz.UserQuizNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userQuiz.ValidationUserQuizException;
import com.coolSchool.CoolSchool.models.dto.UserQuizDTO;
import com.coolSchool.CoolSchool.models.entity.UserQuiz;
import com.coolSchool.CoolSchool.repositories.UserQuizRepository;
import com.coolSchool.CoolSchool.services.UserQuizService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class UserQuizServiceImpl implements UserQuizService {
    private final UserQuizRepository userQuizRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public UserQuizServiceImpl(UserQuizRepository userQuizRepository, ModelMapper modelMapper, Validator validator) {
        this.userQuizRepository = userQuizRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public List<UserQuizDTO> getAllUserQuizzes() {
        List<UserQuiz> userQuizs = userQuizRepository.findByDeletedFalse();
        return userQuizs.stream().map(userQuiz -> modelMapper.map(userQuiz, UserQuizDTO.class)).toList();
    }

    @Override
    public UserQuizDTO getUserQuizById(Long id) {
        Optional<UserQuiz> userQuiz = userQuizRepository.findByIdAndDeletedFalse(id);
        if (userQuiz.isPresent()) {
            return modelMapper.map(userQuiz.get(), UserQuizDTO.class);
        }
        throw new UserQuizNotFoundException();
    }

    @Override
    public UserQuizDTO createUserQuiz(UserQuizDTO userQuizDTO) {
        try {
            UserQuiz userQuizEntity = userQuizRepository.save(modelMapper.map(userQuizDTO, UserQuiz.class));
            return modelMapper.map(userQuizEntity, UserQuizDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationUserQuizException(exception.getConstraintViolations());
        }
    }

    @Override
    public UserQuizDTO updateUserQuiz(Long id, UserQuizDTO userQuizDTO) {
        Optional<UserQuiz> existingUserQuizOptional = userQuizRepository.findByIdAndDeletedFalse(id);

        if (existingUserQuizOptional.isEmpty()) {
            throw new UserQuizNotFoundException();
        }

        UserQuiz existingUserQuiz = existingUserQuizOptional.get();
        modelMapper.map(userQuizDTO, existingUserQuiz);

        try {
            existingUserQuiz.setId(id);
            UserQuiz updatedUserQuiz = userQuizRepository.save(existingUserQuiz);
            return modelMapper.map(updatedUserQuiz, UserQuizDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationUserQuizException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteUserQuiz(Long id) {
        Optional<UserQuiz> userQuiz = userQuizRepository.findByIdAndDeletedFalse(id);
        if (userQuiz.isPresent()) {
            userQuiz.get().setDeleted(true);
            userQuizRepository.save(userQuiz.get());
        } else {
            throw new UserQuizNotFoundException();
        }
    }
}

