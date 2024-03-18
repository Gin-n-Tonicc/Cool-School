import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import { IBlog } from '../../../types/interfaces/blogs/IBlog';
import './HomeBlog.scss';
import HomeBlogCard from './home-blog-card/HomeBlogCard';

// The component that displays the 3 most popular blogs on our website
export default function HomeBlog() {
  const { t } = useTranslation();

  // Fetch the most liked blogs on mount
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
              <p>{t('home.our.blog')}</p>
              <h2>{t('home.students.blog')}</h2>
            </div>
          </div>
        </div>
        <div className="row blog-cards-wrapper">
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
