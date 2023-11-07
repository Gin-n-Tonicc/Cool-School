import { IComment } from './IComment';

export interface ICommentsByBlogResponse {
  comments: IComment[];
  totalComments: number;
}
