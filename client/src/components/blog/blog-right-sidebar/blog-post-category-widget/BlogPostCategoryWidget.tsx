import { useTranslation } from 'react-i18next';
import { apiUrlsConfig } from '../../../../config/apiUrls';
import { useFetch } from '../../../../hooks/useFetch';
import { ICategory } from '../../../../types/interfaces/ICategory';
import './BlogPostCategoryWidget.scss';
import BlogPostCategory from './blog-post-category/BlogPostCategory';

export default function BlogPostCategoryWidget() {
  const { t } = useTranslation();

  const { data: categories } = useFetch<ICategory[]>(
    apiUrlsConfig.categories.get,
    []
  );

  return (
    <aside className="single_sidebar_widget post_category_widget">
      <h4 className="widget_title">{t('blogs.categories')}</h4>
      <ul className="list cat-list">
        {categories?.map((x) => (
          <BlogPostCategory key={x.id} name={x.name} />
        ))}
      </ul>
    </aside>
  );
}
