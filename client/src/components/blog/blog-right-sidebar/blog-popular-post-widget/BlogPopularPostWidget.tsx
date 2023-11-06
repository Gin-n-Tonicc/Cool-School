import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { IBlog } from '../../../../types/interfaces/IBlog';
import { timeSince } from '../../../../utils/dateUtilts';
import BlogPopularPost from './blog-popular-post/BlogPopularPost';

const RECENT_BLOGS_COUNT = 3;

export default function BlogPopularPostWidget() {
  const { data: blogs } = useFetch<IBlog[]>(
    apiUrlsConfig.blogs.recent(RECENT_BLOGS_COUNT),
    []
  );

  return (
    <aside className="single_sidebar_widget popular_post_widget">
      <h3 className="widget_title">Recent Blogs</h3>

      {blogs?.map((x) => {
        const date = timeSince(
          new Date(Date.now() - (Date.now() - new Date(x.created_at).getTime()))
        );

        return (
          <BlogPopularPost
            key={x.id}
            img={apiUrlsConfig.files.get(x.picture.url)}
            redirectUrl={PagesEnum.SingleBlog.replace(':id', x.id.toString())}
            title={x.title}
            date={`${date} ago`}
          />
        );
      })}
    </aside>
  );
}
