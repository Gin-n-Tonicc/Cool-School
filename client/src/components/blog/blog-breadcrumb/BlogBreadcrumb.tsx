import './BlogBreadcrumb.scss';

export default function BlogBreadcrumb() {
  return (
    <section className="breadcrumb breadcrumb_bg">
      <div className="container">
        <div className="row">
          <div className="col-lg-12">
            <div className="breadcrumb_iner text-center">
              <div className="breadcrumb_iner_item">
                <h2>Our Blog</h2>
                <p>
                  Home<span>/</span>Blog
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
