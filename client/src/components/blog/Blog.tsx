import './Blog.scss';
import BlogBreadcrumb from './blog-breadcrumb/BlogBreadcrumb';
import BlogLeftSidebar from './blog-left-sidebar/BlogLeftSidebar';
import BlogRightSidebar from './blog-right-sidebar/BlogRightSidebar';

// A component that groups and displays
// all of the components in the blogs page
export default function Blog() {
  return (
    <>
      <BlogBreadcrumb />
      <section className="blog_area section_padding">
        <div className="container">
          <div className="row">
            <BlogLeftSidebar />
            <BlogRightSidebar />
          </div>
        </div>
      </section>
    </>
  );
}
