import { FormEventHandler } from 'react';
import { useSearchParams } from 'react-router-dom';
import './BlogSearchWidget.scss';

export default function BlogSearchWidget() {
  const [_, setSearchParams] = useSearchParams();

  const onSubmit: FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const title = new FormData(e.currentTarget).get('title')?.toString();

    if (title) {
      return setSearchParams((prev) => ({
        ...prev,
        title,
      }));
    }

    setSearchParams((prev) => {
      const { title: _, ...remainingParams } = Object.fromEntries(prev);
      return remainingParams;
    });
  };

  return (
    <aside className="single_sidebar_widget search_widget">
      <form onSubmit={onSubmit}>
        <div className="form-group">
          <div className="input-group mb-3">
            <input
              type="text"
              className="form-control"
              placeholder="Search Keyword"
              name="title"
            />
            <div className="input-group-append">
              <button className="btn" type="button">
                <i className="ti-search"></i>
              </button>
            </div>
          </div>
        </div>
        <button
          className="button rounded-0 primary-bg text-white w-100 btn_1"
          type="submit">
          Search
        </button>
      </form>
    </aside>
  );
}
