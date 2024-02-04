import { Fragment, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useLocaleContext } from '../../../contexts/LocaleContext';
import { useFetch } from '../../../hooks/useFetch';
import { useLinkState } from '../../../hooks/useLinkState';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IQuiz } from '../../../types/interfaces/IQuiz';
import { IQuizAttempt } from '../../../types/interfaces/IQuizAttempt';
import QuizSingle from '../quiz-single/QuizSingle';
import './QuizStart.scss';

export default function QuizStart() {
  const [hasStarted, setHasStarted] = useState(false);
  const [currentAttempt, setCurrentAttempt] = useState<IQuizAttempt | null>(
    null
  );
  const { locale } = useLocaleContext();

  const { id } = useParams();
  const { data } = useLinkState<IQuiz>(apiUrlsConfig.quizzes.getInfoById(id));
  const { data: attempts, get } = useFetch<IQuizAttempt[]>(
    apiUrlsConfig.quizzes.getAttemptsById(id),
    []
  );

  const ongoingAttempt = attempts?.find((x) => !x.completed) || null;

  const { post, response } = useFetch<IQuizAttempt>(
    apiUrlsConfig.quizzes.take(id)
  );

  const nowDate = new Date(Date.now());
  const startDate = new Date(`${data?.startTime}`);
  const endDate = new Date(`${data?.endTime}`);

  const canStartQuiz = nowDate > startDate && startDate < endDate;

  const startQuiz = async () => {
    let attempt = ongoingAttempt;
    let quizResult: IQuizAttempt;

    if (!attempt) {
      quizResult = await post();

      if (response.ok) {
        attempt = quizResult;
      }
    }

    if (attempt) {
      setCurrentAttempt(attempt);
      setHasStarted(true);
    }
  };

  const onQuizFinish = async () => {
    const result = get();
    setHasStarted(false);
    await result;
  };

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

  return (
    <div className="quiz-start-wrapper max-w-4xl mx-auto p-5">
      <div className="quiz-start bg-white p-6 rounded-md">
        <h1 className="text-xl font-bold mb-4">{data?.title}</h1>
        <h2 className="text-lg font-semibold mb-2">{data?.description}</h2>
        <div className="grid grid-cols-2 gap-4 mb-4">
          <div>
            <div className="text-sm">Тестът е отворен между:</div>
            <div className="text-sm">
              {startDate.toLocaleString(locale)} -{' '}
              {endDate.toLocaleString(locale)}
            </div>
          </div>
          <div>
            <div className="text-sm">
              Разрешен брой опити: {data?.attemptLimit}
            </div>
            <div className="text-sm">
              Времеви лимит: {data?.quizDurationInMinutes}мин.
            </div>
          </div>
        </div>
        <div className="border-t border-gray-300 pt-4">
          <h3 className="text-xl font-semibold mt-10 mb-2 text-center">
            Обобщение на предишните Ви опити
          </h3>
          <div className="attempt-grid grid grid-cols-3 gap-4 mb-4 text-center overflow-y-scroll">
            <div className="text-base font-semibold">Състояние</div>
            <div className="text-base font-semibold">Оценка / </div>
            <div className="text-base font-semibold">Процент</div>
            {attempts?.map((x) => (
              <Fragment key={x.id}>
                <div className="text-sm">{!x.completed && 'Не '}Завършен</div>
                <div className="text-sm">{x.totalMarks.toFixed(2)}</div>
                <div className="text-sm">10%</div>
              </Fragment>
            ))}

            {!attempts?.length && <div className="text-sm">Няма опити</div>}
          </div>
          <div className="text-sm font-semibold mb-2 text-center">
            Финалната Ви оценка за този тест е {highestGrade.toFixed(2)}
            /15,00.
          </div>
          <div className="my-10 mb-0 border-t border-gray-300 pt-4">
            <h2 className="text-lg">Цялостна забележка</h2>
            <div className="text-sm mb-4">Няма все още!</div>
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
              {canStartQuiz ? 'ЗАПОЧНИ ТЕСТ' : 'ТЕСТЪТ ВМОМЕНТА Е ЗАТВОРЕН'}
            </button>
          </div>
        </div>
        <div className="flex justify-between items-center border-t border-gray-300 pt-4 mt-4">
          <Link
            to={PagesEnum.Courses}
            className="flex gap-2 text-sm text-gray-600">
            <span>&#9664;</span>
            <span>Обратно към курсовете</span>
          </Link>
          <div className="flex gap-2 text-sm text-gray-600">
            <span>{data?.description}</span>
          </div>
        </div>
      </div>
    </div>
  );
}
