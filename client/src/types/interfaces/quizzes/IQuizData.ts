import { IQuestionAndAnswers } from './IQuestionAndAnswers';
import { IQuiz } from './IQuiz';

export interface IQuizData {
  quizDTO: IQuiz;
  data: IQuestionAndAnswers[];
}
