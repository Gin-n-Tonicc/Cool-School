import Select, { SingleValue } from 'react-select';

export interface CategoryOption {
  readonly value: string;
  readonly label: string;
}

interface CategorySelectProps {
  value?: SingleValue<CategoryOption>;
  categories: CategoryOption[];
  onCategoryChange: (v: number) => void;
  placeholder?: string;
  customOnChange?: (v: string | undefined) => void;
}

export default function CategorySelect(props: CategorySelectProps) {
  return (
    <>
      <Select
        value={props.value}
        inputId="category-select"
        name="category-select"
        placeholder={props.placeholder}
        options={props.categories}
        onChange={(newValue) => {
          const value = newValue?.value;

          if (props.customOnChange) {
            props.customOnChange(value);
            return;
          }

          const numberVal = isNaN(Number(value)) ? -1 : Number(value);
          props.onCategoryChange(numberVal);
        }}
        theme={(theme) => ({
          ...theme,
          colors: {
            ...theme.colors,
            primary: 'rgba(249, 183, 0, 1)',
            primary25: 'rgba(249, 183, 0, 0.25)',
            primary50: 'rgba(249, 183, 0, 0.50)',
            primary75: 'rgba(249, 183, 0, 0.75)',
          },
        })}
      />
    </>
  );
}
