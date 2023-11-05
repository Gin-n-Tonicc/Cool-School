import { MouseEventHandler } from 'react';
import { useSearchParams } from 'react-router-dom';

export interface BlogPostCategoryProps {
  name: string;
}

export const CATEGORY_PARAM_KEY = 'category';

export default function BlogPostCategory(props: BlogPostCategoryProps) {
  const [searchParams, setSearchParams] = useSearchParams();
  const isSelected = searchParams.get(CATEGORY_PARAM_KEY) === props.name;

  const onClick: MouseEventHandler = (e) => {
    if (!isSelected) {
      return setSearchParams((prev) => ({
        ...Object.fromEntries(prev),
        [CATEGORY_PARAM_KEY]: props.name,
      }));
    }

    setSearchParams((prev) => {
      const { [CATEGORY_PARAM_KEY]: _, ...remainingParams } =
        Object.fromEntries(prev);
      return remainingParams;
    });
  };

  return (
    <li>
      <span className="d-flex">
        <p onClick={onClick} className={isSelected ? 'selected' : ''}>
          {props.name}
        </p>
      </span>
    </li>
  );
}
