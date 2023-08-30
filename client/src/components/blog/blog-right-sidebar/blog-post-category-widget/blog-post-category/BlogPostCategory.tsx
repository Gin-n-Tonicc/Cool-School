export interface BlogPostCategoryProps {
  redirectUrl: string;
  name: string;
  count: number;
}

export default function BlogPostCategory(props: BlogPostCategoryProps) {
  return (
    <li>
      <a href={props.redirectUrl} className="d-flex">
        <p>{props.name} </p>
        <p>({props.count.toString().padStart(2, '0')})</p>
      </a>
    </li>
  );
}
