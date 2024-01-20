import { PropsWithChildren } from 'react';
import { CachePolicies, CustomOptions, Provider, useFetch } from 'use-http';
import { apiUrlsConfig } from '../../config/apiUrls';
import { useAuthContext } from '../../contexts/AuthContext';
import { useErrorContext } from '../../contexts/ErrorContext';
import { IUser } from '../../types/interfaces/IUser';
import {
  deleteJwtCookie,
  deleteRefreshCookie,
  getJwtCookie,
  getRefreshCookie,
} from '../../utils/cookieUtils';
import { initialAuthUtils } from '../../utils/initialAuthUtils';
import { isJwtExpired } from '../../utils/jwtUtils';

export default function HttpProvider({ children }: PropsWithChildren) {
  const { isAuthenticated } = useAuthContext();

  const { addError } = useErrorContext();

  const { get } = useFetch<IUser>(apiUrlsConfig.auth.refreshToken());

  const removeTokensIfExpired = () => {
    if (isJwtExpired(getJwtCookie())) {
      deleteJwtCookie();
    }

    if (isJwtExpired(getRefreshCookie())) {
      deleteRefreshCookie();
    }
  };

  const refreshToken = async () => {
    await get();
  };

  const options: Partial<CustomOptions> = {
    interceptors: {
      request: async ({ options, url }) => {
        const pathname = new URL(url || '').pathname;

        const isRefreshRequest = pathname.includes(
          apiUrlsConfig.auth.refreshTokenPath
        );

        removeTokensIfExpired();

        const isExpired = !Boolean(getJwtCookie()) && !isAuthenticated;
        if (!isRefreshRequest && isExpired && getRefreshCookie()) {
          await refreshToken();
        }

        if (url?.includes(apiUrlsConfig.apiUrl)) {
          options.credentials = 'include';
        }

        return options;
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
          addError(message);
        }

        return response;
      },
    },
    cachePolicy: CachePolicies.NO_CACHE,
    // default is:
    // cachePolicy: CachePolicies.CACHE_FIRST
  };

  return (
    <Provider url={process.env.REACT_APP_API_URL} options={options}>
      {children}
    </Provider>
  );
}
