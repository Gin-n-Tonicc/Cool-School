import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../../../../config/apiUrls';
import { useFetch } from '../../../../../../../hooks/useFetch';
import { IQuiz } from '../../../../../../../types/interfaces/quizzes/IQuiz';

interface CoursesQuizzesControlProps {
  quiz: IQuiz;
  refreshQuizzes: Function;
}

// The component that displays and handles
// a single uploaded course quiz
export default function CoursesQuizzesControl({
  quiz,
  refreshQuizzes,
}: CoursesQuizzesControlProps) {
  const { t } = useTranslation();

  // Prepare fetch
  const { del, response } = useFetch<string>(
    apiUrlsConfig.quizzes.delete(quiz.id)
  );

  // Handle delete
  const onDelete = async () => {
    if (
      !window.confirm(
        t('courses.quizzes.delete.confirm', { quizTitle: quiz.title })
      )
    ) {
      return;
    }

    await del();

    // After which update quizzes
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
