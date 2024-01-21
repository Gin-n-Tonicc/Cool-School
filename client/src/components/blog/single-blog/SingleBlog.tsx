import { useCallback, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Navigate, useParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { v4 as uuidV4 } from 'uuid';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useAuthContext } from '../../../contexts/AuthContext';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IBlog } from '../../../types/interfaces/IBlog';
import { ICommentsByBlogResponse } from '../../../types/interfaces/ICommentsByBlogResponse';
import BlogBreadcrumb from '../blog-breadcrumb/BlogBreadcrumb';
import './SingleBlog.scss';
import SingleBlogCommentForm from './single-blog-comment-form/SingleBlogCommentForm';
import SingleBlogComments from './single-blog-comments/SingleBlogComments';

const DEFAULT_COMMENT_COUNT = 2;
const COMMENT_INCREMENT = 5;

export default function SingleBlog() {
  const { t } = useTranslation();
  const { id } = useParams();
  const { user } = useAuthContext();

  const {
    data: blog,
    response,
    loading,
  } = useFetch<IBlog>(apiUrlsConfig.blogs.getOne(id), []);

  const [hasLiked, setHasLiked] = useState(false);
  const { response: likedBlogRes, post } = useFetch<IBlog>(
    apiUrlsConfig.blogs.likeBlog(id)
  );

  const [commentCount, setCommentCount] = useState(DEFAULT_COMMENT_COUNT);

  const { data: commentsRes, get } = useFetch<ICommentsByBlogResponse>(
    apiUrlsConfig.comments.getByBlogId(Number(id || -1), commentCount),
    [commentCount]
  );

  const refreshComments = useCallback(async () => {
    await get();
  }, [get]);

  const loadMoreComments = useCallback(() => {
    setCommentCount((prev) => {
      const result = prev + COMMENT_INCREMENT;
      const totalComments = commentsRes?.totalComments || 0;

      if (result > totalComments) {
        return totalComments;
      }

      return result;
    });
  }, [commentsRes, setCommentCount]);

  const likeBlog = useCallback(async () => {
    await post();

    if (likedBlogRes.ok) {
      setHasLiked(true);
    }
  }, [likedBlogRes, post]);

  if (!loading && !response.ok) {
    return <Navigate to={PagesEnum.Blog} />;
  }

  if (!blog) {
    return <></>;
  }

  let totalLikes = blog.liked_users.length;

  if (hasLiked) {
    totalLikes += 1;
  }

  const hasLikedBlog =
    hasLiked || blog.liked_users.some((x) => x.id === user?.id);

  return (
    <>
      {/* <Breadcrumb heading="Single Blog" pageName="Blog" /> */}
      <BlogBreadcrumb />
      <section className="blog_area single-post-area section_padding">
        <div className="container">
          <div className="row">
            <div className="col-lg-8 posts-list">
              <div className="single-post">
                <div className="feature-img">
                  <img
                    className="img-fluid"
                    src={apiUrlsConfig.files.getByUrl(blog.picture.url)}
                    alt=""
                  />
                </div>
                <div className="blog_details">
                  <h2>{blog.title}</h2>
                  <ul className="blog-info-link mt-3 mb-4">
                    <li>
                      <i className="far fa-user"></i> {blog.category.name}
                    </li>
                    <li>
                      <i className="far fa-comments"></i>{' '}
                      {commentsRes?.totalComments} {t('blogs.comments')}
                    </li>
                  </ul>
                  {blog.content
                    .split('\n')
                    .filter((x) => x)
                    .map((x) => {
                      return <p key={uuidV4()}>{x.trim()}</p>;
                    })}
                </div>
              </div>
              <div className="navigation-top">
                <div className="d-sm-flex justify-content-between text-center">
                  <p className="like-info">
                    <span
                      className={
                        'align-middle blog-like-btn' +
                        (hasLikedBlog ? ' liked' : '')
                      }
                      onClick={() => likeBlog()}>
                      <i className="far fa-heart"></i>
                    </span>{' '}
                    {totalLikes} {t('blogs.amount.people.like')}
                  </p>
                  <div className="col-sm-4 text-center my-2 my-sm-0">
                    <p className="comment-count">
                      <span className="align-middle">
                        <i className="far fa-comment"></i>
                      </span>{' '}
                      {commentsRes?.totalComments} {t('blogs.comments')}
                    </p>
                  </div>
                </div>
              </div>
              <div className="blog-author">
                <div className="media align-items-center">
                  {/* <img src={apiUrlsConfig.files.get(blog.owner.)} alt="" /> */}
                  <div className="media-body">
                    <h4>
                      {blog.owner.firstname} ({blog.owner.username})
                    </h4>
                    <p>{blog.owner.description}</p>
                  </div>
                </div>
              </div>
              <SingleBlogComments
                comments={commentsRes?.comments || []}
                totalComments={commentsRes?.totalComments || 0}
                loadMoreComments={loadMoreComments}
              />
              <SingleBlogCommentForm
                blogId={blog.id}
                refreshComments={refreshComments}
              />
            </div>
          </div>
        </div>
      </section>
    </>
  );
}
