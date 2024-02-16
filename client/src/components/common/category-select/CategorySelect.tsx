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
      />
    </>
  );
}
