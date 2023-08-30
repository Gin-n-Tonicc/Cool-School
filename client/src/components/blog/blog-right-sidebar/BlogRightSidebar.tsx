import './BlogRightSidebar.scss';
import BlogNewsletterWidget from './blog-newsletter-widget/BlogNewsletterWidget';
import BlogPopularPostWidget from './blog-popular-post-widget/BlogPopularPostWidget';
import BlogPostCategoryWidget from './blog-post-category-widget/BlogPostCategoryWidget';
import BlogSearchWidget from './blog-search-widget/BlogSearchWidget';
import BlogTagCloudWidget from './blog-tag-cloud-widget/BlogTagCloudWidget';

export default function BlogRightSidebar() {
  return (
    <div className="col-lg-4">
      <div className="blog_right_sidebar">
        <BlogSearchWidget />
        <BlogPostCategoryWidget />
        <BlogPopularPostWidget />
        <BlogTagCloudWidget />
        <BlogNewsletterWidget />
      </div>
    </div>
  );
}
