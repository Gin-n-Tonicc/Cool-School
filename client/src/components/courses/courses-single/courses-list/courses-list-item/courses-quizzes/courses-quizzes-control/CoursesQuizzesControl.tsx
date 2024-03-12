import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../../../../config/apiUrls';
import { useFetch } from '../../../../../../../hooks/useFetch';
import { IQuiz } from '../../../../../../../types/interfaces/quizzes/IQuiz';

interface CoursesQuizzesControlProps {
  quiz: IQuiz;
  refreshQuizzes: Function;
}

export default function CoursesQuizzesControl({
  quiz,
  refreshQuizzes,
}: CoursesQuizzesControlProps) {
  const { t } = useTranslation();

  const { del, response } = useFetch<string>(
    apiUrlsConfig.quizzes.delete(quiz.id)
  );

  const onDelete = async () => {
    if (
      !window.confirm(
        t('courses.quizzes.delete.confirm', { quizTitle: quiz.title })
      )
    ) {
      return;
    }

    await del();
    if (response.ok) {
      refreshQuizzes();
    }
  };

  return (
    <div className="resource-control">
      <i onClick={onDelete} className="fas fa-minus-circle"></i>
    </div>
  );
}
