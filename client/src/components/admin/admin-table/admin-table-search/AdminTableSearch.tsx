import { SubmitHandler, useForm } from 'react-hook-form';
import { IDefaultObject } from '../../../../types/interfaces/IDefaultObject';

export type AdminSearchValues = IDefaultObject<number | string>;

interface AdminSearchFormValues {
  search: string;
}

interface AdminTableSearchProps {
  onSubmit: SubmitHandler<AdminSearchValues>;
  columnsLowercased: string[];
}

const SEPARATOR_SEARCH = '//';
const SEPARATOR_VALUES = '=';

export default function AdminTableSearch(props: AdminTableSearchProps) {
  const { register, handleSubmit } = useForm<AdminSearchFormValues>({
    defaultValues: {
      search: '',
    },
  });

  // id{separator1}1{separator2}firstName{separator1}Mike{separator2}lastName{separator1}Ehrmantraut
  // id=1//firstName=Mike//lastName=Ehrmantraut
  const onSubmit: SubmitHandler<AdminSearchFormValues> = (
    v: AdminSearchFormValues
  ) => {
    const searchResult: AdminSearchValues = v.search
      .split(SEPARATOR_SEARCH)
      .filter((x) => x)
      .map((x) => x.split(SEPARATOR_VALUES).filter((x) => x))
      .reduce((acc, [key, value]) => {
        if (!key || !value) {
          return acc;
        }

        const newKey = key.toLowerCase().trim();
        if (!props.columnsLowercased.includes(newKey)) {
          return acc;
        }

        let newValue: number | string = value;

        if (!isNaN(newValue as any)) {
          newValue = Number(value);
        } else {
          newValue = newValue.toLowerCase().trim();
        }

        const objToMerge: AdminSearchValues = {};
        objToMerge[newKey] = newValue;

        return Object.assign(acc, objToMerge);
      }, {});

    props.onSubmit(searchResult);
  };

  return (
    <form className="admin-form-group" onSubmit={handleSubmit(onSubmit)}>
      <span className="fa fa-search admin-form-control-feedback"></span>
      <input
        {...register('search')}
        type="text"
        className="form-control"
        placeholder="Search (col=val//)"
      />
    </form>
  );
}
