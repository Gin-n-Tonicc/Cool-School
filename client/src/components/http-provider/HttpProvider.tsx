import { PropsWithChildren } from 'react';
import { CustomOptions, Provider } from 'use-http';
import { useAuthContext } from '../../contexts/AuthContext';
import { IAuthStorage } from '../../interfaces/IAuthStorage';

export default function HttpProvider({ children }: PropsWithChildren) {
  const { user } = useAuthContext();
  const refreshTokenPath = '/auth/refresh-token';

  const options: Partial<CustomOptions> = {
    interceptors: {
      request: ({ options, url }) => {
        const pathname = new URL(url || '').pathname;

        let customOptions = {
          headers: {
            'Content-Type': 'application/json',
          },
        };

        const shouldAddAuthHeader = pathname.includes(refreshTokenPath);
        const token = decideWhichToken(user, shouldAddAuthHeader);

        if (token) {
          Object.assign(customOptions.headers, {
            Authorization: 'Bearer ' + token,
          });
        }

        return Object.assign(options, customOptions);
      },
      response: async ({ response }) => {
        // TODO: Parse errors
        return response;
      },
    },
  };

  return (
    <Provider url={process.env.REACT_APP_API_URL} options={options}>
      {children}
    </Provider>
  );
}

function decideWhichToken(
  user: Partial<IAuthStorage>,
  shouldAddAuthHeader: boolean
): string | null {
  let token: string | null = null;

  if (!shouldAddAuthHeader && user.accessToken) {
    token = user.accessToken;
  }

  if (shouldAddAuthHeader && user.refreshToken) {
    token = user.refreshToken;
  }

  return token;
}
