package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.answer.AnswerNotFoundException;
import com.coolSchool.coolSchool.exceptions.questions.QuestionNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;
import com.coolSchool.coolSchool.models.entity.Answer;
import com.coolSchool.coolSchool.repositories.AnswerRepository;
import com.coolSchool.coolSchool.repositories.QuestionRepository;
import com.coolSchool.coolSchool.services.AnswerService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final MessageSource messageSource;

    public AnswerServiceImpl(AnswerRepository answerRepository, ModelMapper modelMapper, QuestionRepository questionRepository, MessageSource messageSource) {
        this.answerRepository = answerRepository;
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<AnswerDTO> getAllAnswers() {
        // Retrieve all non-deleted answers from the repository and map them to DTOs
        List<Answer> answers = answerRepository.findByDeletedFalse();
        return answers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }

    @Override
    public AnswerDTO getAnswerById(Long id) {
        // Retrieve the answer by ID if it exists and is not deleted, otherwise throw exception
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(id);
        if (answer.isPresent()) {
            return modelMapper.map(answer.get(), AnswerDTO.class);
        }
        throw new AnswerNotFoundException(messageSource);
    }

    @Override
    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        // Create a new answer after validating the associated question ID
        answerDTO.setId(null);
        questionRepository.findByIdAndDeletedFalse(answerDTO.getQuestionId()).orElseThrow(() -> new QuestionNotFoundException(messageSource));

        Answer answerEntity = answerRepository.save(modelMapper.map(answerDTO, Answer.class));
        return modelMapper.map(answerEntity, AnswerDTO.class);
    }

    @Override
    public AnswerDTO updateAnswer(Long id, AnswerDTO answerDTO) {
        // Update an existing answer after ensuring it exists and the associated question ID is valid
        Optional<Answer> existingAnswerOptional = answerRepository.findByIdAndDeletedFalse(id);

        if (existingAnswerOptional.isEmpty()) {
            throw new AnswerNotFoundException(messageSource);
        }
        questionRepository.findByIdAndDeletedFalse(answerDTO.getQuestionId()).orElseThrow(() -> new QuestionNotFoundException(messageSource));

        Answer existingAnswer = existingAnswerOptional.get();
        modelMapper.map(answerDTO, existingAnswer);

        existingAnswer.setId(id);
        Answer updatedAnswer = answerRepository.save(existingAnswer);
        return modelMapper.map(updatedAnswer, AnswerDTO.class);
    }

    @Override
    public void deleteAnswer(Long id) {
        // Soft delete an answer by setting its 'deleted' flag to true
        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(id);
        if (answer.isPresent()) {
            answer.get().setDeleted(true);
            answerRepository.save(answer.get());
        } else {
            throw new AnswerNotFoundException(messageSource);
        }
    }

    public List<AnswerDTO> getCorrectAnswersByQuestionId(Long questionId) {
        // Retrieve all correct answers for a given question and map them to DTOs
        List<Answer> correctAnswers = answerRepository.findCorrectAnswersByQuestionId(questionId);
        return correctAnswers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }

    public List<AnswerDTO> getAnswersByQuestionId(Long questionId) {
        // Retrieve all answers for a given question and map them to DTOs
        List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId);
        return answers.stream().map(answer -> modelMapper.map(answer, AnswerDTO.class)).toList();
    }
}
