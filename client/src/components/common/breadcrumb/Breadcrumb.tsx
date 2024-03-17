import './Breadcrumb.scss';

export interface BreadcrumbProps {
  heading: string;
  pageName: string;
}

// An abstract breadcrumb component
export default function Breadcrumb(props: BreadcrumbProps) {
  return (
    <section className="breadcrumb breadcrumb_bg">
      <div className="container">
        <div className="row">
          <div className="col-lg-12">
            <div className="breadcrumb_iner text-center">
              <div className="breadcrumb_iner_item">
                <h2>{props.heading}</h2>
                <p>
                  Home<span>/</span>
                  {props.pageName}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
