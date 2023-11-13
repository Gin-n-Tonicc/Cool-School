import Select from 'react-select';

interface CategoryOption {
  readonly value: string;
  readonly label: string;
}

interface BlogCategorySelectProps {
  categories: CategoryOption[];
  onCategoryChange: (v: string | undefined) => void;
}

export default function BlogCategorySelect(props: BlogCategorySelectProps) {
  return (
    <>
      <Select
        aria-labelledby="aria-label"
        inputId="aria-example-input"
        name="aria-live-color"
        options={props.categories}
        onChange={(newValue) => {
          props.onCategoryChange(newValue?.value);
        }}
      />
    </>
  );
}
