import { useEffect } from 'react';
import { CachePolicies, useFetch } from 'use-http';
import { apiUrlsConfig } from '../config/apiUrls';
import { useAuthContext } from '../contexts/AuthContext';
import { IUser } from '../types/interfaces/IUser';
import { initialAuthUtils } from '../utils/initialAuthUtils';

export default function useAuthenticate(shouldLogoutUser: boolean = true) {
  const { user, loginUser, logoutUser } = useAuthContext();

  const { get, response, loading } = useFetch<IUser>(apiUrlsConfig.auth.me, {
    cachePolicy: CachePolicies.NO_CACHE,
  });

  useEffect(() => {
    async function fetchApi() {
      const data = await get();

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
