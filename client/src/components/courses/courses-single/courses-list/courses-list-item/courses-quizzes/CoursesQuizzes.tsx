import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../../../../config/apiUrls';
import { useFetch } from '../../../../../../hooks/useFetch';
import { PagesEnum } from '../../../../../../types/enums/PagesEnum';
import { IQuiz } from '../../../../../../types/interfaces/quizzes/IQuiz';
import CoursesQuizzesControl from './courses-quizzes-control/CoursesQuizzesControl';

interface CoursesQuizzesProps {
  subsectionId: number;
  isOwner: boolean;
}

export default function CoursesQuizzes(props: CoursesQuizzesProps) {
  const { data: quizzes, get } = useFetch<IQuiz[]>(
    apiUrlsConfig.quizzes.getBySubsection(props.subsectionId),
    []
  );

  return (
    <ul>
      {quizzes?.map((x) => (
        <li
          key={x.id}
          className="list-group-item d-flex justify-content-between align-items-center">
          {props.isOwner && (
            <CoursesQuizzesControl quiz={x} key={x.id} refreshQuizzes={get} />
          )}
          <p>{x.title}</p>
          <p>Възможни опити: {x.attemptLimit}</p>
          <Link to={PagesEnum.QuizStart.replace(':id', `${x.id}`)} state={x}>
            ОТВАРЯНЕ
          </Link>
        </li>
      ))}
    </ul>
  );
}
