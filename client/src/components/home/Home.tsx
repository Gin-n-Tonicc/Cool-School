import HomeBanner from './home-banner/HomeBanner';
import HomeBlog from './home-blog/HomeBlog';
import HomeFeatures from './home-features/HomeFeatures';
import HomeLearningFeature from './home-learning-feature/HomeLearningFeature';
import HomeLearning from './home-learning/HomeLearning';

// The component that displays and groups all of
// the components used in the home page
export default function Home() {
  return (
    <>
      <HomeBanner />
      <HomeFeatures />
      <HomeLearning />
      <HomeLearningFeature />
      <HomeBlog />
    </>
  );
}
