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
    const url = new URL(`${baseUrl}/blogs/search`);

    if (titleSearch) {
      url.searchParams.append('title', titleSearch);
    }

    if (categorySearch) {
      url.searchParams.append('category', categorySearch);
    }

    return url.toString();
  },
});

const categoriesPaths = Object.seal({
  get: `${baseUrl}/categories/all`,
});

export const apiUrlsConfig = Object.seal({
  admin: adminPaths,
  auth: authPaths,
  blogs: blogsPaths,
  categories: categoriesPaths,
});
