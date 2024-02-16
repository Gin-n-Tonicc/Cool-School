import { IQuestionAndAnswers } from './IQuestionAndAnswers';
import { IQuiz } from './IQuiz';
import { IUserQuizProgress } from './IUserQuizProgress';

export interface IQuizQuestionsAndAnswers {
  quiz: IQuiz;
  questions: IQuestionAndAnswers[];
  userQuizProgresses: IUserQuizProgress[];
}
