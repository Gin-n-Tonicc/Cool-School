import { Fragment, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link, useParams } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useLocaleContext } from '../../../contexts/LocaleContext';
import { useFetch } from '../../../hooks/useFetch';
import { useLinkState } from '../../../hooks/useLinkState';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IQuiz } from '../../../types/interfaces/quizzes/IQuiz';
import { IQuizAttempt } from '../../../types/interfaces/quizzes/IQuizAttempt';
import QuizSingle from '../quiz-single/QuizSingle';
import './QuizStart.scss';

// The component that displays a
// landing page of a single quiz
export default function QuizStart() {
  const { t } = useTranslation();
  const [hasStarted, setHasStarted] = useState(false);
  const [currentAttempt, setCurrentAttempt] = useState<IQuizAttempt | null>(
    null
  );
  const { locale } = useLocaleContext();

  const { id } = useParams();

  // Get quiz (from state or fetch)
  const { data } = useLinkState<IQuiz>(apiUrlsConfig.quizzes.getInfoById(id));

  // Fetch quiz attempts on mount
  const { data: attempts, get } = useFetch<IQuizAttempt[]>(
    apiUrlsConfig.quizzes.getAttemptsById(id),
    []
  );

  // Prepare fetch
  const { post, response } = useFetch<IQuizAttempt>(
    apiUrlsConfig.quizzes.take(id)
  );

  const startDate = new Date(`${data?.startTime}`);
  const endDate = new Date(`${data?.endTime}`);
  const nowDate = new Date(Date.now());

  const canStartQuiz = nowDate > startDate && nowDate < endDate;
  const ongoingAttempt = attempts?.find((x) => !x.completed) || null;

  // On quiz start
  const startQuiz = async () => {
    let attempt = ongoingAttempt;
    let quizResult: IQuizAttempt;

    // Create an attempt if an ongoing attempt is not found
    if (!attempt) {
      quizResult = await post();

      if (response.ok) {
        attempt = quizResult;
      }
    }

    // Update state with correct attempt
    if (attempt) {
      setCurrentAttempt(attempt);
      setHasStarted(true);
    }
  };

  // Start quiz finish, update state, wait for quiz to finish
  const onQuizFinish = async () => {
    const result = get();
    setHasStarted(false);
    await result;
  };

  // Actual started quiz
  if (hasStarted && currentAttempt && data) {
    return (
      <QuizSingle
        quizId={data?.id}
        currentAttempt={currentAttempt}
        onQuizFinishEvent={onQuizFinish}
      />
    );
  }

  const highestGrade =
    attempts?.sort((a, b) => b.totalMarks - a.totalMarks)[0]?.totalMarks || 0;

  const totalMarks = data?.totalMarks || 0;
  const redirectUrl = data?.courseId
    ? PagesEnum.SingleCourse.replace(':id', `${data.courseId}`)
    : PagesEnum.Courses;

  return (
    <div className="quiz-start-wrapper max-w-4xl mx-auto p-5">
      <div className="quiz-start bg-white p-6 rounded-md">
        <h1 className="text-xl font-bold mb-4">{data?.title}</h1>
        <h2 className="text-lg font-semibold mb-2">{data?.description}</h2>
        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <div className="text-sm">{t('quizzes.start.open.between')}:</div>
            <div className="text-sm">
              {startDate.toLocaleString(locale)} -{' '}
              {endDate.toLocaleString(locale)}
            </div>
          </div>
          <div>
            <div className="text-sm">
              {t('quizzes.start.max.attempts')}: {data?.attemptLimit}
            </div>
            <div className="text-sm">
              {t('quizzes.start.time.limit')}: {data?.quizDurationInMinutes}мин.
            </div>
          </div>
        </div>
        <div className="border-t border-gray-300 pt-4">
          <h3 className="text-xl font-semibold mt-10 mb-2 text-center">
            {t('quizzes.start.summary')}
          </h3>
          <div className="attempt-grid grid grid-cols-3 gap-4 mb-4 text-center overflow-y-scroll">
            <div className="text-base font-semibold">
              {t('quizzes.start.state')}
            </div>
            <div className="text-base font-semibold">
              {t('quizzes.start.grade')} / {totalMarks.toFixed(2)}
            </div>
            <div className="text-base font-semibold">
              {t('quizzes.start.percentage')}
            </div>
            {/* Display each attempt data (completed, totalMarks, percentage) */}
            {attempts?.map((x) => (
              <Fragment key={x.id}>
                <div className="text-sm">
                  {!x.completed && `${t('quizzes.start.uncompleted')} `}
                  {t('quizzes.start.completed')}
                </div>
                <div className="text-sm">{x.totalMarks.toFixed(2)}</div>
                <div className="text-sm">
                  {((x.totalMarks / totalMarks) * 100).toFixed(2)}%
                </div>
              </Fragment>
            ))}

            {!attempts?.length && (
              <div className="text-sm">{t('quizzes.start.no.attempts')}</div>
            )}
          </div>

          <div className="my-10 mb-0 border-t border-gray-300 pt-4">
            <h2 className="text-lg">{t('quizzes.start.feedback')}</h2>
            <div className="text-sm mb-4">
              {' '}
              <div className="text-sm font-semibold mb-2">
                {t('quizzes.start.grade.final')} {highestGrade.toFixed(2)}/
                {totalMarks.toFixed(2)}.
              </div>
            </div>
          </div>
          <div className="flex flex-col gap-2">
            <button
              className="bg-blue-600 text-white px-4 py-2 rounded btn_1"
              onClick={startQuiz}
              disabled={!canStartQuiz}
              style={
                canStartQuiz
                  ? {}
                  : {
                      opacity: 0.7,
                      cursor: 'not-allowed',
                    }
              }>
              {canStartQuiz
                ? t('quizzes.start.button')
                : t('quizzes.start.button.disabled')}
            </button>
          </div>
        </div>
        <div className="flex justify-between items-center border-t border-gray-300 pt-4 mt-4">
          <Link to={redirectUrl} className="flex gap-2 text-sm text-gray-600">
            <span>&#9664;</span>
            <span>{t('quizzes.start.back.to.course.button')}</span>
          </Link>
          <div className="flex gap-2 text-sm text-gray-600">
            <span>{data?.description}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
