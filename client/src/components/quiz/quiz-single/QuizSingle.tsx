import { useCallback, useEffect, useState } from 'react';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useErrorContext } from '../../../contexts/ErrorContext';
import { useFetch } from '../../../hooks/useFetch';
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

export default function QuizSingle(props: QuizSingleProps) {
  const { addError } = useErrorContext();
  const { data, loading: loadingQuiz } = useFetch<IQuizQuestionsAndAnswers>(
    apiUrlsConfig.quizzes.getFullById(props.quizId),
    []
  );

  const [selectedAnswer, setSelectedAnswer] = useState<IAnswer | null>(null);
  const [progresses, setProgresses] = useState<IUserQuizProgress[]>([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);

  const { post: postProgress, loading: loadingProgress } = useFetch<
    IUserQuizProgress[]
  >(apiUrlsConfig.quizzes.saveProgress(data?.quiz.id));

  const { put: putFinish, loading: loadingFinish } = useFetch<any>(
    apiUrlsConfig.quizzes.submit(data?.quiz.id, props.currentAttempt.id)
  );

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

  const isLoading = loadingProgress;
  const quiz = data.quiz;
  const questions = data.questions;

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

  const switchQuestion = async (questionIndex: number) => {
    let currentQuestion = questions[currentQuestionIndex];

    // if (selectedAnswer) {
    //   const userProgress: ISaveUserProgress = {
    //     quizId: quiz.id,
    //     questionId: currentQuestion.question.id,
    //     answerId: selectedAnswer.id,
    //     userId: 1,
    //     id: 0,
    //     attemptId: props.currentAttempt.id,
    //   };

    //   setProgresses(await postProgress(userProgress));
    // }
    await getAndPostProgress(currentQuestion);

    const newIndex = validateIndex(questions.length, questionIndex);
    setCurrentQuestionIndex(newIndex);

    currentQuestion = questions[newIndex];
    const progress = progresses.find(
      (x) => x.questionId === currentQuestion.question.id
    );

    const answer =
      currentQuestion.answers.find((x) => x.id === progress?.answerId) || null;

    setSelectedAnswer(answer);
  };

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

  const onFinish = async () => {
    if (window.confirm('Are you sure you want to submit?')) {
      await onSubmitQuiz();
    }
  };

  const onTimerEnd = async () => {
    addError('Time expired. Submitted automatically!');
    await onSubmitQuiz();
  };

  const currentQuestion = questions[currentQuestionIndex];

  return (
    <section className="quiz-parent">
      <div className="display-container">
        <div className="header">
          <div className="number-of-count">
            <span className="number-of-question">
              Question {currentQuestionIndex + 1} of {questions.length} total
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
              Finish
            </button>
          )}
        </div>
      </div>
    </section>
  );
}
