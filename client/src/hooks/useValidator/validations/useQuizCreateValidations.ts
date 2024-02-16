import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

export default function useQuizCreateValidations() {
  const { t } = useTranslation();

  const TITLE_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.title.invalid.required'),
  };

  const DESCRIPTION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.description.invalid.required'),
  };

  const START_TIME_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.start.time.invalid.required'),
  };

  const END_TIME_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.end.time.invalid.required'),
  };

  const ATTEMPT_LIMIT_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.attempt.limit.invalid.required'),
    min: {
      value: 1,
      message: t('quizzes.attempt.limit.invalid.min'),
    },
  };

  const QUIZ_DURATION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.duration.invalid.required'),
    min: {
      value: 5,
      message: t('quizzes.duration.invalid.min'),
    },
    max: {
      value: 300,
      message: t('quizzes.duration.invalid.max'),
    },
  };

  const QUESTION_MARKS_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.marks.invalid.required'),
    min: {
      value: 1,
      message: t('quizzes.marks.invalid.min'),
    },
  };

  const ANSWER_TEXT_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.answer.text.invalid.required'),
  };

  const ANSWER_QUESTION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.answer.question.invalid.required'),
  };

  return {
    TITLE_VALIDATIONS,
    DESCRIPTION_VALIDATIONS,
    START_TIME_VALIDATIONS,
    END_TIME_VALIDATIONS,
    ATTEMPT_LIMIT_VALIDATIONS,
    QUIZ_DURATION_VALIDATIONS,
    QUESTION_MARKS_VALIDATIONS,
    ANSWER_TEXT_VALIDATIONS,
    ANSWER_QUESTION_VALIDATIONS,
  };
}
