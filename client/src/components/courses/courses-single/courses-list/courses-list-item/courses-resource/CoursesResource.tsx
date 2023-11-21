import { useFetch } from 'use-http';
import { apiUrlsConfig } from '../../../../../../config/apiUrls';
import { IResource } from '../../../../../../types/interfaces/IResource';
import './CoursesResource.scss';

interface CoursesResourceProps {
  resource: IResource;
  refreshResources: Function;
  isOwner: boolean;
}

export default function CoursesResource({
  resource,
  refreshResources,
  isOwner,
}: CoursesResourceProps) {
  const { del, response } = useFetch<string>(
    apiUrlsConfig.resources.delete(resource.id)
  );

  const file = resource.file;
  const uploadedFileLink = apiUrlsConfig.files.getByUrl(file.url);
  const name = `${resource.name} (.${file.name.split('.').pop()})`;

  const handleDownload = () => {
    fetch(uploadedFileLink, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/pdf',
      },
    })
      .then((response) => response.blob())
      .then((blob) => {
        const url = window.URL.createObjectURL(new Blob([blob]));

        const link = document.createElement('a');
        link.href = url;
        link.download = file.name;

        document.body.appendChild(link);

        link.click();

        link.parentNode?.removeChild(link);
      });
  };

  const handleDelete = async () => {
    if (
      !window.confirm(`Are you sure you want to delete resource \'${name}\'`)
    ) {
      return;
    }

    await del();
    if (response.ok) {
      refreshResources();
    }
  };

  return (
    <li className="list-group-item d-flex justify-content-between align-items-center">
      {isOwner && (
        <div className="resource-control">
          <i onClick={handleDelete} className="fas fa-minus-circle"></i>
        </div>
      )}
      <p>{name}</p>

      <button
        onClick={handleDownload}
        className="btn_1 badge badge-primary badge-pill">
        Download
      </button>
    </li>
  );
}
