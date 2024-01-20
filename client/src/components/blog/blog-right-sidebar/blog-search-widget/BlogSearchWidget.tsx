import { FormEventHandler } from 'react';
import { useTranslation } from 'react-i18next';
import { useSearchParams } from 'react-router-dom';
import './BlogSearchWidget.scss';

export const TITLE_PARAM_KEY = 'title';

export default function BlogSearchWidget() {
  const { t } = useTranslation();
  const [_, setSearchParams] = useSearchParams();

  const onSubmit: FormEventHandler<HTMLFormElement> = (e) => {
    e.preventDefault();

    const value = new FormData(e.currentTarget)
      .get(TITLE_PARAM_KEY)
      ?.toString();

    if (value) {
      return setSearchParams((prev) => ({
        ...Object.fromEntries(prev),
        [TITLE_PARAM_KEY]: value,
      }));
    }

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
