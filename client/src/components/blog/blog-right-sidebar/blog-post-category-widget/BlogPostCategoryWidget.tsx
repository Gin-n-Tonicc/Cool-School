import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { ICategory } from '../../../../types/interfaces/ICategory';
import './BlogPostCategoryWidget.scss';
import BlogPostCategory from './blog-post-category/BlogPostCategory';

export default function BlogPostCategoryWidget() {
  const { data: categories } = useFetch<ICategory[]>(
    apiUrlsConfig.categories.get,
    []
  );

  return (
    <aside className="single_sidebar_widget post_category_widget">
      <h4 className="widget_title">Categories</h4>
      <ul className="list cat-list">
        {categories?.map((x) => (
          <BlogPostCategory key={x.id} name={x.name} />
        ))}
        {/* <BlogPostCategory redirectUrl="#" name="Restaurant food" count={37} />
        <BlogPostCategory redirectUrl="#" name="Travel news" count={10} />
        <BlogPostCategory redirectUrl="#" name="Modern technology" count={3} />
        <BlogPostCategory redirectUrl="#" name="Product" count={11} />
        <BlogPostCategory redirectUrl="#" name="Inspiration" count={21} />
        <BlogPostCategory redirectUrl="#" name="Health Care" count={9} /> */}
      </ul>
    </aside>
  );
}
