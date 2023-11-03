const baseUrl = process.env.REACT_APP_API_URL;

const adminPaths = {
  get: (pathName: string) => `${baseUrl}${pathName}/all`,
  post: (pathName: string) => `${baseUrl}${pathName}/create`,
  updateDelete: (pathName: string) => `${baseUrl}${pathName}`,
};

const authPaths = {
  register: `${baseUrl}/auth/register`,
  login: `${baseUrl}/auth/authenticate`,
  logout: `${baseUrl}/auth/logout`,
  refreshTokenPath: '/auth/refresh-token',
  refreshToken() {
    return `${baseUrl}${this.refreshTokenPath}`;
  },
  me: `${baseUrl}/auth/me`,
};

export const apiUrlsConfig = {
  admin: { ...adminPaths },
  auth: { ...authPaths },
};
