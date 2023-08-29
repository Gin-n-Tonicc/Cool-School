import './BlogTagCloud.scss';

export interface BlogTagCloudProps {
  redirectUrl: string;
  text: string;
}

export default function BlogTagCloud(props: BlogTagCloudProps) {
  return (
    <li className="blog_tag_cloud">
      <a href={props.redirectUrl}>{props.text}</a>
    </li>
  );
}
