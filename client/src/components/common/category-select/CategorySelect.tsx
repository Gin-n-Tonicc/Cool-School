import Select from 'react-select';

interface CategoryOption {
  readonly value: string;
  readonly label: string;
}

interface CategorySelectProps {
  categories: CategoryOption[];
  onCategoryChange: (v: number) => void;
}

export default function CategorySelect(props: CategorySelectProps) {
  return (
    <>
      <Select
        inputId="category-select"
        name="category-select"
        options={props.categories}
        onChange={(newValue) => {
          const value = newValue?.value;
          const numberVal = isNaN(Number(value)) ? -1 : Number(value);

          props.onCategoryChange(numberVal);
        }}
      />
    </>
  );
}
