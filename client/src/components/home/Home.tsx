import HomeBanner from './home-banner/HomeBanner';
import HomeBlog from './home-blog/HomeBlog';
import HomeFeatures from './home-features/HomeFeatures';
import HomeLearningFeature from './home-learning-feature/HomeLearningFeature';
import HomeLearning from './home-learning/HomeLearning';
import HomeMemberCounter from './home-member-counter/HomeMemberCounter';

export default function Home() {
  return (
    <>
      <HomeBanner />
      <HomeFeatures />
      <HomeLearning />
      <HomeMemberCounter />
      <HomeLearningFeature />
      <HomeBlog />
    </>
  );
}
