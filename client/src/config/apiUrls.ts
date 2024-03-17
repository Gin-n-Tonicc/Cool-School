// FILE TO STORE ALL OF THE USED API URLS

const baseApiUrl = process.env.REACT_APP_API_URL || '';
const baseOAuthUrl = process.env.REACT_APP_OAUTH_URL || '';

const adminPaths = Object.seal({
  get: (pathName: string) => `${baseApiUrl}${pathName}/all`,
  post: (pathName: string) => `${baseApiUrl}${pathName}/create`,
  updateDelete: (pathName: string) => `${baseApiUrl}${pathName}`,
});

const authPaths = Object.seal({
  register: `${baseApiUrl}/auth/register`,
  login: `${baseApiUrl}/auth/authenticate`,
  logout: `${baseApiUrl}/auth/logout`,
  refreshTokenPath: '/auth/refresh-token',
  completeOAuth: `${baseApiUrl}/auth/complete-oauth`,
  refreshToken() {
    return `${baseApiUrl}${this.refreshTokenPath}`;
  },
  me: `${baseApiUrl}/auth/me`,
  forgotPassword: (email: string) =>
    `${baseApiUrl}/auth/forgot-password?email=${encodeURIComponent(email)}`,
  resetPassword: (token: string, newPassword: string) =>
    `${baseApiUrl}/auth/password-reset?token=${encodeURIComponent(
      token
    )}&newPassword=${encodeURIComponent(newPassword)}`,
});

const blogsPaths = Object.seal({
  upload: `${baseApiUrl}/blogs/create`,
  search: (titleSearch: string | null, categorySearch: string | null) => {
    const url = new URL(`${baseApiUrl}/blogs/search/all`);

    if (titleSearch) {
      url.searchParams.append('title', titleSearch);
    }

    if (categorySearch) {
      url.searchParams.append('category', categorySearch);
    }

    return url.toString();
  },
  recent: (n: number) => `${baseApiUrl}/blogs/mostRecent/${n}`,
  getOne: (id: number | string | undefined) => `${baseApiUrl}/blogs/${id}`,
  likeBlog: (id: number | string | undefined) =>
    `${baseApiUrl}/blogs/like/${id}`,
  mostLiked: `${baseApiUrl}/blogs/sort/likes`,
  generateAIContent: `${baseApiUrl}/blogs/generate/AI/text`,
  recommendAICategory: `${baseApiUrl}/blogs/recommend-category/AI`,
});

const categoriesPaths = Object.seal({
  get: `${baseApiUrl}/categories/all`,
});

const filesPaths = Object.seal({
  base: `${baseApiUrl}/files`,
  getByFilename(fileName: string) {
    return `${this.base}/${fileName}`;
  },
  getByUrl(url: string) {
    const imgArr = url.split('/');
    const fileName = imgArr[imgArr.length - 1];
    return this.getByFilename(fileName);
  },
  upload() {
    return `${this.base}/upload`;
  },
});

const commentsPaths = Object.seal({
  getByBlogId: (blogId: number, n: number) =>
    `${baseApiUrl}/comments/blog/${blogId}?n=${n}`,
  post: `${baseApiUrl}/comments/create`,
});

const coursesPaths = Object.seal({
  getAll: `${baseApiUrl}/courses/all`,
  upload: `${baseApiUrl}/courses/create`,
  getOne: (id: number | string | undefined) => `${baseApiUrl}/courses/${id}`,
  canEnroll: (id: number | string | undefined) =>
    `${baseApiUrl}/courses/canEnroll/${id}`,
  enroll: (id: number | string | undefined) =>
    `${baseApiUrl}/courses/enroll/${id}`,
});

const reviewsPaths = Object.seal({
  getByCourse: (courseId: number | string | undefined) =>
    `${baseApiUrl}/reviews/all/${courseId}`,
  create: `${baseApiUrl}/reviews/create`,
});

const courseSubsectionsPaths = Object.seal({
  getById: (id: number | string | undefined) =>
    `${baseApiUrl}/courseSubsections/${id}`,
  getByCourse: (courseId: number) =>
    `${baseApiUrl}/courseSubsections/course/${courseId}`,
  create: `${baseApiUrl}/courseSubsections/create`,
  put: (id: number) => `${baseApiUrl}/courseSubsections/${id}`,
  delete: (id: number) => `${baseApiUrl}/courseSubsections/${id}`,
});

const resourcesPaths = Object.seal({
  getBySubsection: (subsectionId: number) =>
    `${baseApiUrl}/resources/subsection/${subsectionId}`,
  create: `${baseApiUrl}/resources/create`,
  delete: (id: number) => `${baseApiUrl}/resources/${id}`,
});

const oAuthPaths = Object.seal({
  google: `${baseOAuthUrl}/google`,
});

const quizzesPaths = Object.seal({
  getBySubsection: (subsectionId: number) =>
    `${baseApiUrl}/quizzes/subsection/${subsectionId}`,
  getInfoById: (id: number | string | undefined) =>
    `${baseApiUrl}/quizzes/info/${id}`,
  getFullById: (id: number | string | undefined) =>
    `${baseApiUrl}/quizzes/${id}`,
  getAttemptsById: (id: number | string | undefined) =>
    `${baseApiUrl}/quizzes/all/attempts/${id}`,
  take: (quizId: number | string | undefined) =>
    `${baseApiUrl}/quizzes/${quizId}/take`,
  saveProgress: (quizId: number | string | undefined) =>
    `${baseApiUrl}/quizzes/${quizId}/save-progress`,
  submit: (quizId: number | string | undefined, attemptId: number) =>
    `${baseApiUrl}/quizzes/${quizId}/submit/${attemptId}`,
  create: `${baseApiUrl}/quizzes/create`,
  delete: (quizId: number) => `${baseApiUrl}/quizzes/${quizId}`,
});

export const apiUrlsConfig = Object.seal({
  apiUrl: baseApiUrl,
  admin: adminPaths,
  auth: authPaths,
  blogs: blogsPaths,
  categories: categoriesPaths,
  files: filesPaths,
  comments: commentsPaths,
  courses: coursesPaths,
  reviews: reviewsPaths,
  courseSubsections: courseSubsectionsPaths,
  resources: resourcesPaths,
  quizzes: quizzesPaths,
  oAuth: oAuthPaths,
});
