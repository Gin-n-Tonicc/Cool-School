import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useFetch } from '../../../../hooks/useFetch';
import { LanguageEnum } from '../../../../types/enums/LanguageEnum';
import { PagesEnum } from '../../../../types/enums/PagesEnum';
import { IBlog } from '../../../../types/interfaces/blogs/IBlog';
import { timeSince } from '../../../../utils/dateUtils';
import BlogPopularPost from './blog-popular-post/BlogPopularPost';

const RECENT_BLOGS_COUNT = 3;

// The widget that contains the {n} recent blogs
export default function BlogPopularPostWidget() {
  const { t, i18n } = useTranslation();

  // Map that contains -----------------
  // Time (in seconds): Appropriate text
  const divisors = useMemo(
    () => ({
      31536000: t('date.years'),
      2592000: t('date.months'),
      86400: t('date.days'),
      3600: t('date.hours'),
      60: t('date.minutes'),
    }),
    [t]
  );

  // Fetch blogs on mount
  const { data: blogs } = useFetch<IBlog[]>(
    apiUrlsConfig.blogs.recent(RECENT_BLOGS_COUNT),
    []
  );

  return (
    <aside className="single_sidebar_widget popular_post_widget">
      <h3 className="widget_title">{t('blogs.recent')}</h3>

      {blogs?.map((x) => {
        // Calculate how much time has passed since the blog creation
        const date = timeSince(
          new Date(
            Date.now() - (Date.now() - new Date(x.created_at).getTime())
          ),
          divisors,
          t('date.seconds')
        );

        const ago = t('date.ago');

        const dateMassage =
          i18n.language === LanguageEnum.ENGLISH
            ? `${date} ${ago}`
            : `${ago} ${date}`;

        // Display each recent blog
        return (
          <BlogPopularPost
            key={x.id}
            img={apiUrlsConfig.files.getByUrl(x.picture.url)}
            redirectUrl={PagesEnum.SingleBlog.replace(':id', x.id.toString())}
            title={x.title}
            date={dateMassage}
          />
        );
      })}
    </aside>
  );
}
