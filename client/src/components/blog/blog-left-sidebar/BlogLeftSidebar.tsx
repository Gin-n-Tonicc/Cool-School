import { useCallback, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { IBlog } from '../../../types/interfaces/IBlog';
import BlogItem, { BlogItemProps } from '../blog-item/BlogItem';
import BlogPagination from '../blog-right-sidebar/blog-pagination/BlogPagination';
import { CATEGORY_PARAM_KEY } from '../blog-right-sidebar/blog-post-category-widget/blog-post-category/BlogPostCategory';
import { TITLE_PARAM_KEY } from '../blog-right-sidebar/blog-search-widget/BlogSearchWidget';

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

  useEffect(() => {
    setUrl(getUrl());
  }, [getUrl]);

  const mockedBlog: BlogItemProps = {
    img: require('./single_blog_1.png'),
    title: 'Google inks pact for new 35-storey office',
    summary:
      "That dominion stars lights dominion divide years for fourth have don't stars is that he earth it first without heaven in place seed it second morning saying.",
    category: 'Travel, Lifestyle',
    commentCount: 3,
    date: new Date(),
  };

  return (
    <div className="col-lg-8 mb-5 mb-lg-0">
      <div className="blog_left_sidebar">
        {blogs?.map((x) => (
          <BlogItem
            key={x.id}
            {...{ ...mockedBlog, ...x }}
            date={new Date(x.created_at)}
          />
        ))}

        <BlogItem {...mockedBlog} />
        <BlogPagination />
      </div>
    </div>
  );
}
