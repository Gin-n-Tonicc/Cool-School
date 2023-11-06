import { useCallback, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { usePagination } from '../../../hooks/usePagination';
import { IBlog } from '../../../types/interfaces/IBlog';
import BlogItem from '../blog-item/BlogItem';
import BlogPagination from '../blog-right-sidebar/blog-pagination/BlogPagination';
import { CATEGORY_PARAM_KEY } from '../blog-right-sidebar/blog-post-category-widget/blog-post-category/BlogPostCategory';
import { TITLE_PARAM_KEY } from '../blog-right-sidebar/blog-search-widget/BlogSearchWidget';

const PAGE_SIZE = 5;

export default function BlogLeftSidebar() {
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
  const { data: blogs } = useFetch<IBlog[]>(url, [url]);

  const {
    list: paginatedBlogs,
    pages,
    currentPage,
    togglePage,
    nextPage,
    previousPage,
  } = usePagination<IBlog>(blogs, PAGE_SIZE);

  useEffect(() => {
    setUrl(getUrl());
  }, [getUrl]);

  return (
    <div className="col-lg-8 mb-5 mb-lg-0">
      <div className="blog_left_sidebar">
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
