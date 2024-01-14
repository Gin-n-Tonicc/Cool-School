const baseUrl = process.env.REACT_APP_API_URL || '';

const adminPaths = Object.seal({
  get: (pathName: string) => `${baseUrl}${pathName}/all`,
  post: (pathName: string) => `${baseUrl}${pathName}/create`,
  updateDelete: (pathName: string) => `${baseUrl}${pathName}`,
});

const authPaths = Object.seal({
  register: `${baseUrl}/auth/register`,
  login: `${baseUrl}/auth/authenticate`,
  logout: `${baseUrl}/auth/logout`,
  refreshTokenPath: '/auth/refresh-token',
  refreshToken() {
    return `${baseUrl}${this.refreshTokenPath}`;
  },
  me: `${baseUrl}/auth/me`,
});

const blogsPaths = Object.seal({
  upload: `${baseUrl}/blogs/create`,
  search: (titleSearch: string | null, categorySearch: string | null) => {
    const url = new URL(`${baseUrl}/blogs/search/all`);

    if (titleSearch) {
      url.searchParams.append('title', titleSearch);
    }

    if (categorySearch) {
      url.searchParams.append('category', categorySearch);
    }

    return url.toString();
  },
  recent: (n: number) => `${baseUrl}/blogs/mostRecent/${n}`,
  getOne: (id: number | string | undefined) => `${baseUrl}/blogs/${id}`,
  likeBlog: (id: number | string | undefined) => `${baseUrl}/blogs/like/${id}`,
  mostLiked: `${baseUrl}/blogs/sort/likes`,
});

const categoriesPaths = Object.seal({
  get: `${baseUrl}/categories/all`,
});

const filesPaths = Object.seal({
  base: `${baseUrl}/files`,
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
    `${baseUrl}/comments/blog/${blogId}?n=${n}`,
  post: `${baseUrl}/comments/create`,
});

const coursesPaths = Object.seal({
  getAll: `${baseUrl}/courses/all`,
  upload: `${baseUrl}/courses/create`,
  getOne: (id: number | string | undefined) => `${baseUrl}/courses/${id}`,
  canEnroll: (id: number | string | undefined) =>
    `${baseUrl}/courses/canEnroll/${id}`,
  enroll: (id: number | string | undefined) =>
    `${baseUrl}/courses/enroll/${id}`,
});

const reviewsPaths = Object.seal({
  getByCourse: (courseId: number | string | undefined) =>
    `${baseUrl}/reviews/all/${courseId}`,
  create: `${baseUrl}/reviews/create`,
});

const courseSubsectionsPaths = Object.seal({
  getByCourse: (courseId: number) =>
    `${baseUrl}/courseSubsections/course/${courseId}`,
  create: `${baseUrl}/courseSubsections/create`,
  put: (id: number) => `${baseUrl}/courseSubsections/${id}`,
  delete: (id: number) => `${baseUrl}/courseSubsections/${id}`,
});

const resourcesPaths = Object.seal({
  getBySubsection: (subsectionId: number) =>
    `${baseUrl}/resources/subsection/${subsectionId}`,
  create: `${baseUrl}/resources/create`,
  delete: (id: number) => `${baseUrl}/resources/${id}`,
});

export const apiUrlsConfig = Object.seal({
  apiUrl: baseUrl,
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
});
