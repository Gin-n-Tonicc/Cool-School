import { PropsWithChildren } from 'react';
import { CachePolicies, CustomOptions, Provider, useFetch } from 'use-http';
import { useAuthContext } from '../../contexts/AuthContext';
import { IAuthRefreshResponse } from '../../interfaces/IAuthRefreshResponse';
import { IAuthStorage } from '../../interfaces/IAuthStorage';
import { isJwtExpired } from '../../utils/jwtUtils';

export default function HttpProvider({ children }: PropsWithChildren) {
  const { user, isAuthenticated, setAccessToken, removeRefreshToken } =
    useAuthContext();

  const { post, response } = useFetch<IAuthRefreshResponse>(
    `${process.env.REACT_APP_API_URL}/auth/refresh-token`,
    { cachePolicy: CachePolicies.NO_CACHE }
  );

  const refreshTokenPath = '/auth/refresh-token';

  const removeTokensIfExpired = () => {
    if (isJwtExpired(user.accessToken)) {
      setAccessToken(undefined);
    }

    if (isJwtExpired(user.refreshToken)) {
      removeRefreshToken();
    }
  };

  const refreshToken = async () => {
    const data = await post({ refreshToken: user.refreshToken });

    if (response.ok) {
      setAccessToken(data.accessToken);
    }
  };

  const options: Partial<CustomOptions> = {
    interceptors: {
      request: async ({ options, url }) => {
        const pathname = new URL(url || '').pathname;

        let customOptions = {
          headers: {
            'Content-Type': 'application/json',
          },
        };

        const isRefreshRequest = pathname.includes(refreshTokenPath);
        const isExpired = Boolean(user.accessToken) && !isAuthenticated;

        if (!isRefreshRequest && isExpired) {
          await refreshToken();
        }

        removeTokensIfExpired();
        /* 
          TODO:
          Check for session validity when loading the page
        */

        if (user.accessToken) {
          Object.assign(customOptions.headers, {
            Authorization: 'Bearer ' + user.accessToken,
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
  isRefreshRequest: boolean
): string | null {
  let token: string | null = null;

  if (!isRefreshRequest && user.accessToken) {
    token = user.accessToken;
  }

  if (isRefreshRequest && user.refreshToken) {
    token = user.refreshToken;
  }

  return token;
}
