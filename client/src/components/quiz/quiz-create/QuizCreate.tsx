import { useCallback, useEffect, useRef, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Navigate, useNavigate, useParams } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { useFetch } from '../../../hooks/useFetch';
import { useLinkState } from '../../../hooks/useLinkState';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { ICourse } from '../../../types/interfaces/courses/ICourse';
import { ICourseSubsection } from '../../../types/interfaces/courses/ICourseSubsection';
import { IAnswer } from '../../../types/interfaces/quizzes/IAnswer';
import { IQuestionAndAnswers } from '../../../types/interfaces/quizzes/IQuestionAndAnswers';
import { IQuiz } from '../../../types/interfaces/quizzes/IQuiz';
import { IQuizData } from '../../../types/interfaces/quizzes/IQuizData';
import Spinner from '../../common/spinner/Spinner';
import './QuizCreate.scss';
import AnswerForm, {
  AnswerFormAnswer,
  AnswerState,
} from './answer-form/AnswerForm';
import QuestionForm, {
  QuestionFormQuestion,
  QuestionState,
} from './question-form/QuestionForm';
import QuizForm from './quiz-form/QuizForm';

// The component that displays
// and handles all of the other 3 forms (quiz, question, answer)
// and is responsible for the way quiz data is stored and sent to the server
export default function QuizCreate() {
  const { t } = useTranslation();
  const { courseId, subsectionId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuthContext();

  const ref = useRef<HTMLButtonElement>(null);

  // Fetch subsection on mount
  const { data: subsection, response: subsectionResponse } =
    useFetch<ICourseSubsection>(
      apiUrlsConfig.courseSubsections.getById(subsectionId),
      []
    );

  // Prepare fetches
  const { data: course } = useLinkState<ICourse>(
    apiUrlsConfig.courses.getOne(courseId)
  );

  const { post, response: postQuizResponse } = useFetch<IQuiz>(
    apiUrlsConfig.quizzes.create
  );

  // Prepare state to store all of the quiz data
  const [questionsAnswers, setQuestionsAnswers] = useState<
    IQuestionAndAnswers[]
  >([]);
  const [questions, setQuestions] = useState<QuestionState>({});
  const [answers, setAnswers] = useState<AnswerState>({});

  const seedQuestionsAndAnswers = useCallback(() => {
    const questionsAndAnswers: IQuestionAndAnswers[] = [];

    for (const question of Object.values(questions)) {
      // Find answers based on the custom question ID
      // and map to the required type
      const filtered: IAnswer[] = Object.values(answers)
        .filter((x) => x.customQuestionId === question.customId)
        .map((x) => ({
          text: x.text || '',
          correct: x.isCorrect || false,
          question: 0,
          id: 0,
        }));

      // Map question to the required type
      questionsAndAnswers.push({
        question: {
          marks: question.marks || -1,
          description: question.description || '',
          quiz: 0,
          id: 0,
        },
        answers: filtered,
      });
    }

    // Update the state with (quiz, answers)
    setQuestionsAnswers(questionsAndAnswers);
  }, [questions, answers]);

  // Seed questions and answers on mount and on question/answer change
  useEffect(() => {
    seedQuestionsAndAnswers();
  }, [seedQuestionsAndAnswers]);

  if (!course || !subsection) {
    return <Spinner />;
  }

  const isCourseOwner = course.user.id === user.id;

  if (!isCourseOwner || !subsectionResponse.ok) {
    return (
      <Navigate to={PagesEnum.SingleCourse.replace(':id', `${course.id}`)} />
    );
  }

  // Handle quiz creation
  const onCreateFormSubmit = async (data: IQuiz) => {
    const body: IQuizData = {
      quizDTO: data,
      data: questionsAnswers,
    };

    await post(body);
    if (postQuizResponse.ok) {
      navigate(PagesEnum.SingleCourse.replace(':id', `${course.id}`));
    }
  };

  // Get question data from child and add it to the current stored questions
  const onQuestionSubmit = (data: QuestionFormQuestion) => {
    if (data.description) {
      const description = data.description;

      setQuestions((prev) => ({
        ...prev,
        [description]: data,
      }));
    }
  };

  // Get answer data from child and add it to the current stored answers
  const onAnswerSubmit = (data: AnswerFormAnswer) => {
    if (data.text) {
      const text = data.text;

      setAnswers((prev) => ({
        ...prev,
        [text]: data,
      }));
    }
  };

  return (
    <section className="signup">
      <div className="sign-container">
        <div className="signup-content">
          <div className="signup-form create-blog-form quiz-form-wrapper">
            <div>
              <h2 className="form-title">{t('quizzes.create.heading')}</h2>
              <QuizForm
                onSubmit={onCreateFormSubmit}
                submitRef={ref}
                subsectionId={subsection?.id}
              />

              <div className="quiz-create-subforms">
                <QuestionForm onSubmit={onQuestionSubmit} />
                <AnswerForm onSubmit={onAnswerSubmit} questions={questions} />
              </div>
            </div>

            <button className="btn_1" onClick={() => ref.current?.click()}>
              {t('quizzes.create.button')}
            </button>
          </div>
        </div>
      </div>
    </section>
  );
}
