package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.userAnswer.UserAnswerNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userAnswer.ValidationUserAnswerException;
import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;
import com.coolSchool.CoolSchool.models.entity.UserAnswer;
import com.coolSchool.CoolSchool.repositories.UserAnswerRepository;
import com.coolSchool.CoolSchool.services.UserAnswerService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class UserAnswerServiceImpl implements UserAnswerService {
    private final UserAnswerRepository userAnswerRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public UserAnswerServiceImpl(UserAnswerRepository userAnswerRepository, ModelMapper modelMapper, Validator validator) {
        this.userAnswerRepository = userAnswerRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public List<UserAnswerDTO> getAllUserAnswers() {
        List<UserAnswer> userAnswers = userAnswerRepository.findByDeletedFalse();
        return userAnswers.stream().map(userAnswer -> modelMapper.map(userAnswer, UserAnswerDTO.class)).toList();
    }

    @Override
    public UserAnswerDTO getUserAnswerById(Long id) {
        Optional<UserAnswer> userAnswer = userAnswerRepository.findByIdAndDeletedFalse(id);
        if (userAnswer.isPresent()) {
            return modelMapper.map(userAnswer.get(), UserAnswerDTO.class);
        }
        throw new UserAnswerNotFoundException();
    }

    @Override
    public UserAnswerDTO createUserAnswer(UserAnswerDTO userAnswerDTO) {
        try {
            UserAnswer userAnswerEntity = userAnswerRepository.save(modelMapper.map(userAnswerDTO, UserAnswer.class));
            return modelMapper.map(userAnswerEntity, UserAnswerDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationUserAnswerException(exception.getConstraintViolations());
        }
    }

    @Override
    public UserAnswerDTO updateUserAnswer(Long id, UserAnswerDTO userAnswerDTO) {
        Optional<UserAnswer> existingUserAnswerOptional = userAnswerRepository.findByIdAndDeletedFalse(id);

        if (existingUserAnswerOptional.isEmpty()) {
            throw new UserAnswerNotFoundException();
        }

        UserAnswer existingUserAnswer = existingUserAnswerOptional.get();
        modelMapper.map(userAnswerDTO, existingUserAnswer);

        try {
            existingUserAnswer.setId(id);
            UserAnswer updatedUserAnswer = userAnswerRepository.save(existingUserAnswer);
            return modelMapper.map(updatedUserAnswer, UserAnswerDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationUserAnswerException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteUserAnswer(Long id) {
        Optional<UserAnswer> userAnswer = userAnswerRepository.findByIdAndDeletedFalse(id);
        if (userAnswer.isPresent()) {
            userAnswer.get().setDeleted(true);
            userAnswerRepository.save(userAnswer.get());
        } else {
            throw new UserAnswerNotFoundException();
        }
    }
}
