import { Link } from 'react-router-dom';
import { apiUrlsConfig } from '../../../../../../config/apiUrls';
import { useFetch } from '../../../../../../hooks/useFetch';
import { PagesEnum } from '../../../../../../types/enums/PagesEnum';
import { IQuiz } from '../../../../../../types/interfaces/IQuiz';

interface CoursesQuizzesProps {
  subsectionId: number;
  isOwner: boolean;
}

export default function CoursesQuizzes(props: CoursesQuizzesProps) {
  const { data: quizzes } = useFetch<IQuiz[]>(
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
            <div className="resource-control">
              <i className="fas fa-minus-circle"></i>
            </div>
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
