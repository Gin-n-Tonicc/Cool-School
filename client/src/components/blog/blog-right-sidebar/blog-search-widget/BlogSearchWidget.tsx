import { FormEventHandler } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import './BlogSearchWidget.scss';

export const TITLE_PARAM_KEY = 'title';

// The component that displays a search form
// and handles the search action by changing the search params
export default function BlogSearchWidget() {
  const { t } = useTranslation();
  const [_, setSearchParams] = useSearchParams();

  // Handler which triggers whenever search happens
  const onSubmit: FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const value = new FormData(e.currentTarget)
      .get(TITLE_PARAM_KEY)
      ?.toString();

    // Add the 'title' search param to the previous ones
    if (value) {
      return setSearchParams((prev) => ({
        ...Object.fromEntries(prev),
        [TITLE_PARAM_KEY]: value,
      }));
    }

    // Extract the 'title' param from the search params
    setSearchParams((prev) => {
      const { [TITLE_PARAM_KEY]: _, ...remainingParams } =
        Object.fromEntries(prev);

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
              placeholder={t('blogs.search.keyword')}
              name="title"
            />
            <div className="input-group-append">
              <button className="btn" type="submit">
                <i className="ti-search"></i>
              </button>
            </div>
          </div>
        </div>
        <button
          className="button rounded-0 primary-bg text-white w-100 btn_1"
          type="submit">
          {t('blogs.search.button')}
        </button>
      </form>
    </aside>
  );
}
