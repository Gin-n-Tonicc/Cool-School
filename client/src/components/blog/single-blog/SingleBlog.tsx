import { useCallback } from 'react';
import { Navigate, useParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { v4 as uuidV4 } from 'uuid';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { PagesEnum } from '../../../types/enums/PagesEnum';
import { IBlog } from '../../../types/interfaces/IBlog';
import { IComment } from '../../../types/interfaces/IComment';
import Breadcrumb from '../../common/breadcrumb/Breadcrumb';
import Spinner from '../../common/spinner/Spinner';
import './SingleBlog.scss';
import SingleBlogCommentForm from './single-blog-comment-form/SingleBlogCommentForm';
import SingleBlogComments from './single-blog-comments/SingleBlogComments';

// TODO: Add text when no comments are available
// TODO: Add text when no blogs are available
// TODO: Add blog like system
// TODO: Add load more button on the comments (increment by 10?)

export default function SingleBlog() {
  const { id } = useParams();

  const {
    data: blog,
    response,
    loading,
  } = useFetch<IBlog>(apiUrlsConfig.blogs.getOne(id), []);

  const { data: comments, get } = useFetch<IComment[]>(
    apiUrlsConfig.comments.getByBlogId(Number(id || -1)),
    []
  );

  const refreshComments = useCallback(async () => {
    await get();
  }, [get]);

  if (!loading && !response.ok) {
    return <Navigate to={PagesEnum.Blog} />;
  }

  if (!blog) {
    return <Spinner />;
  }

  return (
    <>
      <Breadcrumb heading="Single Blog" pageName="Blog" />
      <section className="blog_area single-post-area section_padding">
        <div className="container">
          <div className="row">
            <div className="col-lg-8 posts-list">
              <div className="single-post">
                <div className="feature-img">
                  <img
                    className="img-fluid"
                    src={apiUrlsConfig.files.get(blog.picture.url)}
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
                      <i className="far fa-comments"></i> {comments?.length}{' '}
                      Comments
                    </li>
                  </ul>
                  {blog.content.split('\n').map((x) => {
                    return <p key={uuidV4()}>{x.trim()}</p>;
                  })}
                </div>
              </div>
              <div className="navigation-top">
                <div className="d-sm-flex justify-content-between text-center">
                  <p className="like-info">
                    <span className="align-middle">
                      <i className="far fa-heart"></i>
                    </span>{' '}
                    {blog.liked_users.length} people like this
                  </p>
                  <div className="col-sm-4 text-center my-2 my-sm-0">
                    <p className="comment-count">
                      <span className="align-middle">
                        <i className="far fa-comment"></i>
                      </span>{' '}
                      {comments?.length} Comments
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
              <SingleBlogComments comments={comments || []} />
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
