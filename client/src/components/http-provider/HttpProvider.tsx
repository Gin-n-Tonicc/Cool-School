import { PropsWithChildren } from 'react';
import { CachePolicies, CustomOptions, Provider, useFetch } from 'use-http';
import { useAuthContext } from '../../contexts/AuthContext';
import { useErrorContext } from '../../contexts/ErrorContext';
import { IAuthRefreshResponse } from '../../interfaces/IAuthRefreshResponse';
import { initialAuthUtils } from '../../utils/initialAuthUtils';
import { isJwtExpired } from '../../utils/jwtUtils';

export default function HttpProvider({ children }: PropsWithChildren) {
  const { user, isAuthenticated, setAccessToken, removeRefreshToken } =
    useAuthContext();

  const { addError } = useErrorContext();

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

        if (user.accessToken) {
          Object.assign(customOptions.headers, {
            Authorization: 'Bearer ' + user.accessToken,
          });
        }

        return Object.assign(options, customOptions);
      },
      response: async ({ response }) => {
        if (!response.ok) {
          if (
            !initialAuthUtils.hasFinishedInitialAuth() &&
            response.status === 401
          ) {
            return response;
          }

          const message = response.data.message || '';
          addError({ message, unmountAfter: 10000 });
        }

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
