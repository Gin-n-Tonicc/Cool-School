import { useEffect } from 'react';
import { CachePolicies, useFetch } from 'use-http';
import { useAuthContext } from '../contexts/AuthContext';
import { IAuthResponse } from '../types/interfaces/IAuthResponse';
import { initialAuthUtils } from '../utils/initialAuthUtils';

export default function useAuthenticate(shouldLogoutUser: boolean = true) {
  const { user, loginUser, logoutUser } = useAuthContext();

  const { post, response, loading } = useFetch<IAuthResponse>(
    `${process.env.REACT_APP_API_URL}/auth/me`,
    { cachePolicy: CachePolicies.NO_CACHE }
  );

  useEffect(() => {
    async function fetchApi() {
      const data = await post({ accessToken: user.accessToken });
      initialAuthUtils.finishInitialAuth();

      if (response.ok) {
        loginUser(data);
      } else if (shouldLogoutUser) {
        logoutUser();
      }
    }

    fetchApi();
  }, []);

  return { user, loading };
}
