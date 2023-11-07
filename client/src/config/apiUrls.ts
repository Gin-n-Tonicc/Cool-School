const baseUrl = process.env.REACT_APP_API_URL;

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
});

const categoriesPaths = Object.seal({
  get: `${baseUrl}/categories/all`,
});

const filesPaths = Object.seal({
  get: (url: string) => {
    const imgArr = url.split('/');
    return `${baseUrl}/files/${imgArr[imgArr.length - 1]}`;
  },
});

const commentsPaths = Object.seal({
  getByBlogId: (blogId: number, n: number) =>
    `${baseUrl}/comments/blog/${blogId}?n=${n}`,
  post: `${baseUrl}/comments/create`,
});

export const apiUrlsConfig = Object.seal({
  admin: adminPaths,
  auth: authPaths,
  blogs: blogsPaths,
  categories: categoriesPaths,
  files: filesPaths,
  comments: commentsPaths,
});
