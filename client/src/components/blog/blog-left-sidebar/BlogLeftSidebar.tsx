import { useCallback, useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../config/apiUrls';
import { IBlog } from '../../../types/interfaces/IBlog';
import BlogItem, { BlogItemProps } from '../blog-item/BlogItem';
import BlogPagination from '../blog-right-sidebar/blog-pagination/BlogPagination';

export default function BlogLeftSidebar() {
  const [searchParams] = useSearchParams({
    title: '',
    category: '',
  });

  const getUrl = useCallback(() => {
    return apiUrlsConfig.blogs.search(
      searchParams.get('title'),
      searchParams.get('category')
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
        {/* <BlogItem {...mockedBlog} />
        <BlogItem {...mockedBlog} />
        <BlogItem {...mockedBlog} />
        <BlogItem {...mockedBlog} /> */}

        <BlogPagination />
      </div>
    </div>
  );
}
