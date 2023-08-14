import './HomeBlog.scss';
import HomeBlogCard, { HomeBlogCardProps } from './home-blog-card/HomeBlogCard';

export default function HomeBlog() {
  const blogCards: HomeBlogCardProps[] = [
    {
      category: 'Design',
      title: 'Dry Beginning Sea Over Tree',
      summary:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      totalComments: 2,
      totalLikes: 200,
      image: require('./images/blog_1.png'),
    },
    {
      category: 'Developing',
      title: 'All Beginning Air Two Likeness',
      summary:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      totalComments: 3,
      totalLikes: 300,
      image: require('./images/blog_2.png'),
    },
    {
      category: 'Writing',
      title: 'Dry Beginning Sea Over Tree',
      summary:
        'Which whose darkness saying were life unto fish wherein all fish of together called',
      totalComments: 4,
      totalLikes: 400,
      image: require('./images/blog_3.png'),
    },
  ];

  return (
    <section className="blog_part section_padding">
      <div className="container">
        <div className="row justify-content-center">
          <div className="col-xl-5">
            <div className="section_tittle text-center">
              <p>Our Blog</p>
              <h2>Students Blog</h2>
            </div>
          </div>
        </div>
        <div className="row">
          {blogCards.map((x) => (
            <HomeBlogCard {...x} />
          ))}
        </div>
      </div>
    </section>
  );
}
