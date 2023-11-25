import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { IBlog } from '../../../types/interfaces/IBlog';
import './HomeBlog.scss';
import HomeBlogCard from './home-blog-card/HomeBlogCard';

export default function HomeBlog() {
  const { data, response, loading } = useFetch<IBlog[]>(
    apiUrlsConfig.blogs.mostLiked,
    []
  );

  return (
    <section className="blog_part section_padding">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-xl-5">
            <div className="section_tittle text-center">
              <p>Our Blog</p>
              <h2>Students Blog</h2>
            </div>
          </div>
        </div>
        <div className="row">
          {!loading && response.ok
            ? data?.map((x) => (
                <HomeBlogCard
                  key={x.id}
                  id={x.id}
                  category={x.category.name}
                  title={x.title}
                  summary={x.summary}
                  totalComments={x.commentCount}
                  totalLikes={x.liked_users.length}
                  image={apiUrlsConfig.files.getByUrl(x.picture.url)}
                />
              ))
            : null}
        </div>
      </div>
    </section>
  );
}
