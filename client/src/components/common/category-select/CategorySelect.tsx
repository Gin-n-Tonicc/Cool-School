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

// The component that displays a dropdown with values based on the props
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

          // Use OnChangeHandler with no value processing
          if (props.customOnChange) {
            props.customOnChange(value);
            return;
          }

          // Cast value to number and pass it to OnChangeHandler
          const numberVal = isNaN(Number(value)) ? -1 : Number(value);
          props.onCategoryChange(numberVal);
        }}
        theme={(theme) => ({
          ...theme,
          colors: {
            ...theme.colors,
            primary: 'rgba(244, 122, 7, 1)',
            primary25: 'rgba(244, 122, 7, 0.25)',
            primary50: 'rgba(244, 122, 7, 0.50)',
            primary75: 'rgba(244, 122, 7, 0.75)',
          },
        })}
      />
    </>
  );
}
