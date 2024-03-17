import { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useErrorContext } from '../../../contexts/ErrorContext';
import { useFetch } from '../../../hooks/useFetch';
import { ErrorTypeEnum } from '../../../types/enums/ErrorTypeEnum';
import { IAnswer } from '../../../types/interfaces/quizzes/IAnswer';
import { IQuestionAndAnswers } from '../../../types/interfaces/quizzes/IQuestionAndAnswers';
import { IQuizAttempt } from '../../../types/interfaces/quizzes/IQuizAttempt';
import { IQuizQuestionsAndAnswers } from '../../../types/interfaces/quizzes/IQuizQuestionsAndAnswers';
import { ISaveUserProgress } from '../../../types/interfaces/quizzes/ISaveUserProgress';
import { IUserAnswer } from '../../../types/interfaces/quizzes/IUserAnswer';
import { IUserQuizProgress } from '../../../types/interfaces/quizzes/IUserQuizProgress';
import { validateIndex } from '../../../utils/page';
import Spinner from '../../common/spinner/Spinner';
import './QuizSingle.scss';
import QuizSingleTimer from './quiz-single-timer/QuizSingleTimer';

interface QuizSingleProps {
  quizId: number;
  currentAttempt: IQuizAttempt;
  onQuizFinishEvent: Function;
}

// The component that displays a single quiz
export default function QuizSingle(props: QuizSingleProps) {
  const { t } = useTranslation();
  const { addError } = useErrorContext();

  // Fetch quiz data on mount
  const { data, loading: loadingQuiz } = useFetch<IQuizQuestionsAndAnswers>(
    apiUrlsConfig.quizzes.getFullById(props.quizId),
    []
  );

  // Prepare question answer state
  const [selectedAnswer, setSelectedAnswer] = useState<IAnswer | null>(null);
  const [progresses, setProgresses] = useState<IUserQuizProgress[]>([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);

  // Prepare fetches
  const { post: postProgress, loading: loadingProgress } = useFetch<
    IUserQuizProgress[]
  >(apiUrlsConfig.quizzes.saveProgress(data?.quiz.id));

  const { put: putFinish, loading: loadingFinish } = useFetch<any>(
    apiUrlsConfig.quizzes.submit(data?.quiz.id, props.currentAttempt.id)
  );

  // Find answer based on the progress of the questions
  const findAndSetAnswer = useCallback(
    (question: IQuestionAndAnswers, progresses: IUserQuizProgress[]) => {
      const progress = progresses.find(
        (x) => x.questionId === question.question.id
      );

      const answer =
        question.answers.find((x) => x.id === progress?.answerId) || null;
      setSelectedAnswer(answer);
    },
    []
  );

  // On quiz load
  // save all progress, update current question and set the current progress on that question
  useEffect(() => {
    if (data?.userQuizProgresses) {
      const progresses = data.userQuizProgresses;
      setProgresses(progresses);
      setCurrentQuestionIndex(progresses.length - 1);
      findAndSetAnswer(data.questions[progresses.length - 1], progresses);
    }
  }, [loadingQuiz]);

  if (!data) {
    return <Spinner />;
  }

  const isLoading = loadingProgress && loadingFinish;
  const quiz = data.quiz;
  const questions = data.questions;

  // Save user progress and return updated progresses
  const getAndPostProgress = async (currentQuestion: IQuestionAndAnswers) => {
    if (selectedAnswer) {
      const userProgress: ISaveUserProgress = {
        quizId: quiz.id,
        questionId: currentQuestion.question.id,
        answerId: selectedAnswer.id,
        userId: 1,
        id: 0,
        attemptId: props.currentAttempt.id,
      };

      const newProgresses = await postProgress(userProgress);
      setProgresses(newProgresses);
      return newProgresses;
    }

    return progresses;
  };

  // Save user progress and switch the question and find progress (if exists)
  const switchQuestion = async (questionIndex: number) => {
    // Save user progress
    let currentQuestion = questions[currentQuestionIndex];
    await getAndPostProgress(currentQuestion);

    // Find and switch question
    const newIndex = validateIndex(questions.length, questionIndex);
    setCurrentQuestionIndex(newIndex);

    currentQuestion = questions[newIndex];

    // Find progress based on question
    const progress = progresses.find(
      (x) => x.questionId === currentQuestion.question.id
    );

    // Find and set answer based on the progress
    const answer =
      currentQuestion.answers.find((x) => x.id === progress?.answerId) || null;

    setSelectedAnswer(answer);
  };

  // Save quiz progress, submit it with the new progresses
  // and signalize the parent component that the quiz has been submitted
  const onSubmitQuiz = async () => {
    const newProgresses = await getAndPostProgress(
      questions[currentQuestionIndex]
    );

    const userAnswers: IUserAnswer[] = newProgresses.map((x) => ({
      questionId: x.questionId,
      selectedOptionId: x.answerId,
    }));

    await putFinish(userAnswers);
    await props.onQuizFinishEvent();
  };

  // Submit quiz on finish
  const onFinish = async () => {
    if (window.confirm(t('quizzes.submit.confirm'))) {
      await onSubmitQuiz();
    }
  };

  // Alert the user that his time has ran out
  // and then submit the quiz
  const onTimerEnd = async () => {
    addError(t('quizzes.submit.auto'), ErrorTypeEnum.HEADS_UP);
    await onSubmitQuiz();
  };

  const currentQuestion = questions[currentQuestionIndex];

  return (
    <section className="quiz-parent">
      <div className="display-container">
        <div className="header">
          <div className="number-of-count">
            <span className="number-of-question">
              {t('quizzes.single.question')} {currentQuestionIndex + 1}{' '}
              {t('quizzes.single.of')} {questions.length}{' '}
              {t('quizzes.single.total')}
            </span>
          </div>
          <QuizSingleTimer
            onTimerEndEvent={onTimerEnd}
            timeLeft={props.currentAttempt.timeLeft}
            remainingTimeInSeconds={props.currentAttempt.remainingTimeInSeconds}
          />
        </div>
        <div className="question-container">
          <div className="container-mid">
            <p className="question">{currentQuestion.question.description}:</p>
            {currentQuestion.answers.map((x) => (
              <button
                className={
                  'option-div' +
                  (selectedAnswer?.id === x.id ? ` selected` : '')
                }
                onClick={() =>
                  setSelectedAnswer((prev) => (prev?.id !== x.id ? x : null))
                }
                key={x.id}>
                {x.text}
              </button>
            ))}
          </div>
        </div>
        <div className="quiz-arrow-buttons">
          <button
            className="next-button"
            onClick={() => switchQuestion(currentQuestionIndex - 1)}>
            <i className="zmdi zmdi-arrow-left"></i>
          </button>
          {currentQuestionIndex !== questions.length - 1 ? (
            <button
              className="next-button"
              onClick={() => switchQuestion(currentQuestionIndex + 1)}>
              <i className="zmdi zmdi-arrow-right"></i>
            </button>
          ) : (
            <button
              className="next-button"
              disabled={isLoading}
              style={
                !isLoading
                  ? {}
                  : {
                      opacity: 0.7,
                      cursor: 'not-allowed',
                    }
              }
              onClick={onFinish}>
              {t('quizzes.single.finish')}
            </button>
          )}
        </div>
      </div>
    </section>
  );
}
