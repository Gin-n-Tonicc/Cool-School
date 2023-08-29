import BlogItem, { BlogItemProps } from '../blog-item/BlogItem';
import BlogPagination from '../blog-right-sidebar/blog-pagination/BlogPagination';

export default function BlogLeftSidebar() {
  const blogItem: BlogItemProps = {
    img: require('./single_blog_1.png'),
    monthDay: 15,
    month: 'Jan',
    title: 'Google inks pact for new 35-storey office',
    description:
      "That dominion stars lights dominion divide years for fourth have don't stars is that he earth it first without heaven in place seed it second morning saying.",
    category: 'Travel, Lifestyle',
    commentCount: 3,
  };

  return (
    <div className="col-lg-8 mb-5 mb-lg-0">
      <div className="blog_left_sidebar">
        <BlogItem {...blogItem} />
        <BlogItem {...blogItem} />
        <BlogItem {...blogItem} />
        <BlogItem {...blogItem} />
        <BlogItem {...blogItem} />
        {/* <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src="img/blog/single_blog_1.png"
          alt=""
        />
        <a href="#" className="blog_item_date">
          <h3>15</h3>
          <p>Jan</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>Google inks pact for new 35-storey office</h2>
        </a>
        <p>
          That dominion stars lights dominion divide years for fourth
          have don't stars is that he earth it first without heaven in
          place seed it second morning saying.
        </p>
        <ul className="blog-info-link">
          <li>
            <a href="#">
              <i className="far fa-user"></i> Travel, Lifestyle
            </a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i> 03 Comments
            </a>
          </li>
        </ul>
      </div>
    </article>

    <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src="img/blog/single_blog_2.png"
          alt=""
        />
        <a href="#" className="blog_item_date">
          <h3>15</h3>
          <p>Jan</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>Google inks pact for new 35-storey office</h2>
        </a>
        <p>
          That dominion stars lights dominion divide years for fourth
          have don't stars is that he earth it first without heaven in
          place seed it second morning saying.
        </p>
        <ul className="blog-info-link">
          <li>
            <a href="#">
              <i className="far fa-user"></i> Travel, Lifestyle
            </a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i> 03 Comments
            </a>
          </li>
        </ul>
      </div>
    </article>

    <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src="img/blog/single_blog_3.png"
          alt=""
        />
        <a href="#" className="blog_item_date">
          <h3>15</h3>
          <p>Jan</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>Google inks pact for new 35-storey office</h2>
        </a>
        <p>
          That dominion stars lights dominion divide years for fourth
          have don't stars is that he earth it first without heaven in
          place seed it second morning saying.
        </p>
        <ul className="blog-info-link">
          <li>
            <a href="#">
              <i className="far fa-user"></i> Travel, Lifestyle
            </a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i> 03 Comments
            </a>
          </li>
        </ul>
      </div>
    </article>

    <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src="img/blog/single_blog_4.png"
          alt=""
        />
        <a href="#" className="blog_item_date">
          <h3>15</h3>
          <p>Jan</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>Google inks pact for new 35-storey office</h2>
        </a>
        <p>
          That dominion stars lights dominion divide years for fourth
          have don't stars is that he earth it first without heaven in
          place seed it second morning saying.
        </p>
        <ul className="blog-info-link">
          <li>
            <a href="#">
              <i className="far fa-user"></i> Travel, Lifestyle
            </a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i> 03 Comments
            </a>
          </li>
        </ul>
      </div>
    </article>

    <article className="blog_item">
      <div className="blog_item_img">
        <img
          className="card-img rounded-0"
          src="img/blog/single_blog_5.png"
          alt=""
        />
        <a href="#" className="blog_item_date">
          <h3>15</h3>
          <p>Jan</p>
        </a>
      </div>

      <div className="blog_details">
        <a className="d-inline-block" href="single-blog.html">
          <h2>Google inks pact for new 35-storey office</h2>
        </a>
        <p>
          That dominion stars lights dominion divide years for fourth
          have don't stars is that he earth it first without heaven in
          place seed it second morning saying.
        </p>
        <ul className="blog-info-link">
          <li>
            <a href="#">
              <i className="far fa-user"></i> Travel, Lifestyle
            </a>
          </li>
          <li>
            <a href="#">
              <i className="far fa-comments"></i> 03 Comments
            </a>
          </li>
        </ul>
      </div>
    </article> */}

        <BlogPagination />
      </div>
    </div>
  );
}
