import BlogPopularPost from './blog-popular-post/BlogPopularPost';

export default function BlogPopularPostWidget() {
  return (
    <aside className="single_sidebar_widget popular_post_widget">
      <h3 className="widget_title">Recent Post</h3>
      <BlogPopularPost
        img={require('./post_1.png')}
        redirectUrl="#"
        summary="The Amazing Hubble"
        date="02 Hours ago"
      />
      <BlogPopularPost
        img={require('./post_1.png')}
        redirectUrl="#"
        summary="Asteroids telescope"
        date="01 Hours ago"
      />
    </aside>
  );
}
