import {
  camelCaseToWords,
  capitalizeWord,
} from '../../../../utils/stringUtils';

interface AdminEditFormRowProps {
  columnName: string;
  value: string;
}

export default function AdminEditFormRow(props: AdminEditFormRowProps) {
  return (
    <div className="d-flex flex-row align-items-center justify-content-center form-group row">
      <h3 className="text-dark col-sm-12 col-md-2">
        {camelCaseToWords(props.columnName)}
      </h3>
      <div className="col-sm-12 col-md-10">
        <input
          name={props.columnName}
          className="form-control"
          type="text"
          placeholder={capitalizeWord(props.columnName) + '...'}
          defaultValue={props.value}
        />
      </div>
    </div>
  );
}
