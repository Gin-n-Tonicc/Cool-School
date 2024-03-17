import { useCallback, useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { useFetch } from '../../../hooks/useFetch';
import { usePagination } from '../../../hooks/usePagination';
import { IBlog } from '../../../types/interfaces/blogs/IBlog';
import { CATEGORY_PARAM_KEY } from '../blog-right-sidebar/blog-post-category-widget/blog-post-category/BlogPostCategory';
import { TITLE_PARAM_KEY } from '../blog-right-sidebar/blog-search-widget/BlogSearchWidget';
import BlogItem from './blog-item/BlogItem';
import BlogPagination from './blog-pagination/BlogPagination';

const PAGE_SIZE = 5;

// Display paginated blogs
export default function BlogLeftSidebar() {
  const { t } = useTranslation();

  const [searchParams] = useSearchParams({
    [TITLE_PARAM_KEY]: '',
    [CATEGORY_PARAM_KEY]: '',
  });

  const getUrl = useCallback(() => {
    return apiUrlsConfig.blogs.search(
      searchParams.get(TITLE_PARAM_KEY),
      searchParams.get(CATEGORY_PARAM_KEY)
    );
  }, [searchParams]);

  const [url, setUrl] = useState(getUrl());

  // Fetch on mount and on url change
  const { data: blogs } = useFetch<IBlog[]>(url, [url]);

  // Paginate blogs
  const {
    list: paginatedBlogs,
    pages,
    currentPage,
    togglePage,
    nextPage,
    previousPage,
  } = usePagination<IBlog>(blogs, PAGE_SIZE);

  // Whenever url changes set the state to the changed url
  useEffect(() => {
    setUrl(getUrl());
  }, [getUrl]);

  return (
    <div className="col-lg-8 mb-5 mb-lg-0">
      <div className="blog_left_sidebar">
        {paginatedBlogs.length === 0 && (
          <h1 className="text-center">{t('blogs.not.available')}</h1>
        )}
        {paginatedBlogs?.map((x) => (
          <BlogItem
            key={x.id}
            {...x}
            imgUrl={x.picture.url}
            category={x.category.name}
            date={new Date(x.created_at)}
          />
        ))}

        <BlogPagination
          pages={pages}
          currentPage={currentPage}
          togglePage={togglePage}
          nextPage={nextPage}
          previousPage={previousPage}
        />
      </div>
    </div>
  );
}
