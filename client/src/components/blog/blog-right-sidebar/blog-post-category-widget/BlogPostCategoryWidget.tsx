import BlogPostCategory from './blog-post-category/BlogPostCategory';
import './BlogPostCategoryWidget.scss';

export default function BlogPostCategoryWidget() {
  return (
    <aside className="single_sidebar_widget post_category_widget">
      <h4 className="widget_title">Category</h4>
      <ul className="list cat-list">
        <BlogPostCategory redirectUrl="#" name="Restaurant food" count={37} />
        <BlogPostCategory redirectUrl="#" name="Travel news" count={10} />
        <BlogPostCategory redirectUrl="#" name="Modern technology" count={3} />
        <BlogPostCategory redirectUrl="#" name="Product" count={11} />
        <BlogPostCategory redirectUrl="#" name="Inspiration" count={21} />
        <BlogPostCategory redirectUrl="#" name="Health Care" count={9} />
      </ul>
    </aside>
  );
}
