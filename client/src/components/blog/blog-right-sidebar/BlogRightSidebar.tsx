import './BlogRightSidebar.scss';
import BlogPopularPostWidget from './blog-popular-post-widget/BlogPopularPostWidget';
import BlogPostCategoryWidget from './blog-post-category-widget/BlogPostCategoryWidget';
import BlogSearchWidget from './blog-search-widget/BlogSearchWidget';

export default function BlogRightSidebar() {
  return (
    <div className="col-lg-4">
      <div className="blog_right_sidebar">
        <BlogSearchWidget />
        <BlogPostCategoryWidget />
        <BlogPopularPostWidget />
      </div>
    </div>
  );
}
