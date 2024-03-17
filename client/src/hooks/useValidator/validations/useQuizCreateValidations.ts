import { RegisterOptions } from 'react-hook-form';
import { useTranslation } from 'react-i18next';

// The hook that construct validation for quizCreate form fields
// with a message based on the user's selected language
export default function useQuizCreateValidations() {
  const { t } = useTranslation();

  const TITLE_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.title.invalid.required'),
  };

  const DESCRIPTION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.description.invalid.required'),
  };

  const START_TIME_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.start.time.invalid.required'),
  };

  const END_TIME_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.end.time.invalid.required'),
  };

  const ATTEMPT_LIMIT_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.attempt.limit.invalid.required'),
    min: {
      value: 1,
      message: t('quizzes.create.attempt.limit.invalid.min'),
    },
  };

  const QUIZ_DURATION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.duration.invalid.required'),
    min: {
      value: 5,
      message: t('quizzes.create.duration.invalid.min'),
    },
    max: {
      value: 300,
      message: t('quizzes.create.duration.invalid.max'),
    },
  };

  const QUESTION_MARKS_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.marks.invalid.required'),
    min: {
      value: 1,
      message: t('quizzes.create.marks.invalid.min'),
    },
  };

  const ANSWER_TEXT_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.answer.text.invalid.required'),
  };

  const ANSWER_QUESTION_VALIDATIONS: RegisterOptions = {
    required: t('quizzes.create.answer.question.invalid.required'),
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
