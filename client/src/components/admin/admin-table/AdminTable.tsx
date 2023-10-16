import { v4 as uuidV4 } from 'uuid';
import { IObjectWithId } from '../../../types/interfaces/IObjectWithId';
import { camelCaseToWords } from '../../../utils/stringUtils';
import './AdminTable.scss';

export type AdminTableProps = {
  tableName: string;
  list: IObjectWithId[];
};

export default function AdminTable(props: AdminTableProps) {
  const keys = Object.keys(props.list[0]);

  return (
    <div className="pd-20 card-box mb-30 admin-table section_margin">
      <div className="clearfix mb-20 d-flex flex-column justify-content-center align-items-center">
        <h4 className="text-blue h4">{props.tableName} table</h4>
        <span className="admin-btn btn_1" rel="content-y" role="button">
          <i className="fa fa-plus"></i> CREATE
        </span>
      </div>
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              {keys.map((x) => (
                <th scope="col" key={x}>
                  {camelCaseToWords(x)}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {props.list.map((x) => (
              <tr key={x.id}>
                {Object.values(x).map((x) => (
                  <td key={uuidV4()}>{x}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="d-flex flex-row justify-content-between">
        <ul className="pagination admin-pagination">
          <li className="page-item">
            <a className="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
              <span className="sr-only">Previous</span>
            </a>
          </li>
          <li className="page-item">
            <a className="page-link" href="#">
              1
            </a>
          </li>
          <li className="page-item">
            <a className="page-link" href="#">
              2
            </a>
          </li>
          <li className="page-item">
            <a className="page-link" href="#">
              3
            </a>
          </li>
          <li className="page-item">
            <a className="page-link" href="#" aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
              <span className="sr-only">Next</span>
            </a>
          </li>
        </ul>
        <div className="admin-form-group">
          <span className="fa fa-search admin-form-control-feedback"></span>
          <input type="text" className="form-control" placeholder="Search" />
        </div>
      </div>
    </div>
  );
}
