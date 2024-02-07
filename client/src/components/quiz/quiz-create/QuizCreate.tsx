import { useCallback, useEffect, useRef, useState } from 'react';
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

export default function QuizCreate() {
  const { courseId, subsectionId } = useParams();
  const ref = useRef<HTMLButtonElement>(null);
  const navigate = useNavigate();

  const { data: course } = useLinkState<ICourse>(
    apiUrlsConfig.courses.getOne(courseId)
  );

  const {
    data: subsection,
    loading,
    response: subsectionResponse,
  } = useFetch<ICourseSubsection>(
    apiUrlsConfig.courseSubsections.getById(subsectionId),
    []
  );

  const { user } = useAuthContext();

  const [questionsAnswers, setQuestionsAnswers] = useState<
    IQuestionAndAnswers[]
  >([]);

  const [questions, setQuestions] = useState<QuestionState>({});
  const [answers, setAnswers] = useState<AnswerState>({});
  const { post, response: postQuizResponse } = useFetch<IQuiz>(
    apiUrlsConfig.quizzes.create
  );

  const seedQuestionsAndAnswers = useCallback(() => {
    const questionsAndAnswers: IQuestionAndAnswers[] = [];

    for (const question of Object.values(questions)) {
      const filtered: IAnswer[] = Object.values(answers)
        .filter((x) => x.customQuestionId === question.customId)
        .map((x) => ({
          text: x.text || '',
          correct: x.isCorrect || false,
          question: 0,
          id: 0,
        }));

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

    setQuestionsAnswers(questionsAndAnswers);
  }, [questions, answers]);

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

  const onQuestionSubmit = (data: QuestionFormQuestion) => {
    if (data.description) {
      const description = data.description;

      setQuestions((prev) => ({
        ...prev,
        [description]: data,
      }));
    }
  };

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
              <h2 className="form-title">Създай тест</h2>
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
              Създай тест
            </button>
          </div>
        </div>
      </div>
    </section>
  );
}
