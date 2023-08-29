import './Blog.scss';
import BlogLeftSidebar from './blog-left-sidebar/BlogLeftSidebar';
import BlogRightSidebar from './blog-right-sidebar/BlogRightSidebar';

export default function Blog() {
  return (
    <section className="blog_area section_padding">
      <div className="container">
        <div className="row">
          <BlogLeftSidebar />
          <BlogRightSidebar />
        </div>
      </div>
    </section>
  );
}
