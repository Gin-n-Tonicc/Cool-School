import BlogTagCloud from './blog-tag-cloud/BlogTagCloud';

export default function BlogTagCloudWidget() {
  return (
    <aside className="single_sidebar_widget tag_cloud_widget">
      <h4 className="widget_title">Tag Clouds</h4>
      <ul className="list">
        <BlogTagCloud redirectUrl="#" text="love" />
        <BlogTagCloud redirectUrl="#" text="technology" />
        <BlogTagCloud redirectUrl="#" text="travel" />
        <BlogTagCloud redirectUrl="#" text="restaurant" />
        <BlogTagCloud redirectUrl="#" text="life style" />
        <BlogTagCloud redirectUrl="#" text="design" />
        <BlogTagCloud redirectUrl="#" text="illustration" />
      </ul>
    </aside>
  );
}
