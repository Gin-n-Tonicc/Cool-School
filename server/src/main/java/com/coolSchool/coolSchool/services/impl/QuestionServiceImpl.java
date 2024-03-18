package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.questions.QuestionNotFoundException;
import com.coolSchool.coolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.QuestionDTO;
import com.coolSchool.coolSchool.models.entity.Question;
import com.coolSchool.coolSchool.repositories.QuestionRepository;
import com.coolSchool.coolSchool.repositories.QuizRepository;
import com.coolSchool.coolSchool.services.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final ModelMapper modelMapper;
    private final QuizRepository quizRepository;
    private final MessageSource messageSource;

    public QuestionServiceImpl(QuestionRepository questionRepository, ModelMapper modelMapper, QuizRepository quizRepository, MessageSource messageSource) {
        this.questionRepository = questionRepository;
        this.modelMapper = modelMapper;
        this.quizRepository = quizRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<QuestionDTO> getAllQuestions() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        return questions.stream().map(question -> modelMapper.map(question, QuestionDTO.class)).toList();
    }

    @Override
    public QuestionDTO getQuestionById(Long id) {
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(id);
        if (question.isPresent()) {
            return modelMapper.map(question.get(), QuestionDTO.class);
        }
        throw new QuestionNotFoundException(messageSource);
    }

    @Override
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        // Create question to a certain existing quiz
        questionDTO.setId(null);
        quizRepository.findByIdAndDeletedFalse(questionDTO.getQuizId()).orElseThrow(() -> new QuizNotFoundException(messageSource));
        Question questionEntity = questionRepository.save(modelMapper.map(questionDTO, Question.class));
        return modelMapper.map(questionEntity, QuestionDTO.class);
    }

    @Override
    public QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO) {
        // Update question to a certain existing quiz
        Optional<Question> existingQuestionOptional = questionRepository.findByIdAndDeletedFalse(id);

        if (existingQuestionOptional.isEmpty()) {
            throw new QuestionNotFoundException(messageSource);
        }
        quizRepository.findByIdAndDeletedFalse(questionDTO.getQuizId()).orElseThrow(() -> new QuizNotFoundException(messageSource));
        Question existingQuestion = existingQuestionOptional.get();
        modelMapper.map(questionDTO, existingQuestion);

        existingQuestion.setId(id);
        Question updatedQuestion = questionRepository.save(existingQuestion);
        return modelMapper.map(updatedQuestion, QuestionDTO.class);
    }

    @Override
    public void deleteQuestion(Long id) {
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(id);
        if (question.isPresent()) {
            question.get().setDeleted(true);
            questionRepository.save(question.get());
        } else {
            throw new QuestionNotFoundException(messageSource);
        }
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        // Get all questions in a quiz
        return questionRepository.findByQuizId(quizId);
    }
}
