import { SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { IDefaultObject } from '../../../../types/interfaces/common/IDefaultObject';

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

// The component that displays and handles the table search form
export default function AdminTableSearch(props: AdminTableSearchProps) {
  const { t } = useTranslation();

  // Handle form
  const { register, handleSubmit } = useForm<AdminSearchFormValues>({
    defaultValues: {
      search: '',
    },
  });

  // How the separators work:
  // id{separator1}1{separator2}firstName{separator1}Mike{separator2}lastName{separator1}Ehrmantraut
  // id=1//firstName=Mike//lastName=Ehrmantraut
  // Handle form submit
  const onSubmit: SubmitHandler<AdminSearchFormValues> = (
    v: AdminSearchFormValues
  ) => {
    const searchMapResult: AdminSearchValues = v.search
      .split(SEPARATOR_SEARCH)
      .filter((x) => x)
      .map((x) => x.split(SEPARATOR_VALUES).filter((x) => x))
      .reduce((acc, [key, value]) => {
        if (!key || !value) {
          return acc;
        }

        // Remove search based on non-existing columns
        const newKey = key.toLowerCase().trim();
        if (!props.columnsLowercased.includes(newKey)) {
          return acc;
        }

        // Parse search value
        let newValue: number | string = value;

        if (!isNaN(newValue as any)) {
          newValue = Number(value);
        } else {
          newValue = newValue.toLowerCase().trim();
        }

        // Map search value to the acc
        const objToMerge: AdminSearchValues = {};
        objToMerge[newKey] = newValue;

        return Object.assign(acc, objToMerge);
      }, {});

    props.onSubmit(searchMapResult);
  };

  return (
    <form className="admin-form-group" onSubmit={handleSubmit(onSubmit)}>
      <span className="fa fa-search admin-form-control-feedback"></span>
      <input
        {...register('search')}
        type="text"
        className="form-control"
        placeholder={t('admin.table.search')}
      />
    </form>
  );
}
