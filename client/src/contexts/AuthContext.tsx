import { PropsWithChildren, createContext, useContext } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { RolesEnum } from '../types/enums/RolesEnum';
import { IAuthResponse } from '../types/interfaces/auth/IAuthResponse';
import { IAuthStorage } from '../types/interfaces/auth/IAuthStorage';
import { IUser } from '../types/interfaces/auth/IUser';
import { deleteJwtCookie, deleteRefreshCookie } from '../utils/cookieUtils';
import { isJwtExpired } from '../utils/jwtUtils';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  hasFinishedOAuth2: boolean;
  isTeacher: boolean;
  updateUser: (v: IUser) => void;
  loginUser: (v: IAuthResponse) => void;
  logoutUser: () => void;
  removeJwt: () => void;
  removeRefresh: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

// The component that provides all of the children
// with the necessary auth properties and functions
export function AuthProvider({ children }: PropsWithChildren) {
  // Persist auth in local storage
  const { value: auth, setStorageData: setAuth } =
    useLocalStorage<IAuthStorage>('auth', {});

  // Handle user update
  const updateUser: AuthContextType['updateUser'] = (object) => {
    setAuth((oldUser) => ({ ...oldUser, ...object }));
  };

  // Update state to the new user
  const loginUser: AuthContextType['loginUser'] = (authResponse) => {
    const user = authResponse.user;

    setAuth({
      email: user?.email,
      id: user?.id,
      username: user?.username,
      firstname: user?.firstname,
      role: user?.role,
      additionalInfoRequired: user?.additionalInfoRequired,
      accessToken: authResponse.accessToken,
      refreshToken: authResponse.refreshToken,
    });
  };

  // Clear state and delete auth cookies
  const logoutUser: AuthContextType['logoutUser'] = () => {
    setAuth({});
    deleteJwtCookie();
    deleteRefreshCookie();
  };

  // Remove jwt (accessToken) from state and delete jwt cookie
  const removeJwt: AuthContextType['removeJwt'] = () => {
    deleteJwtCookie();
    setAuth((prev) => {
      const { accessToken, ...rest } = prev;
      return rest;
    });
  };

  // Remove refresh (refreshToken) from state and delete refresh cookie
  const removeRefresh: AuthContextType['removeRefresh'] = () => {
    deleteRefreshCookie();
    setAuth((prev) => {
      const { refreshToken, ...rest } = prev;
      return rest;
    });
  };

  // Prepare variables
  const isAuthenticated =
    Boolean(auth.accessToken) && !isJwtExpired(auth.accessToken);
  const hasFinishedOAuth2 =
    isAuthenticated && !Boolean(auth.additionalInfoRequired);

  const isTeacher = auth.role === RolesEnum.TEACHER;

  return (
    <AuthContext.Provider
      value={{
        user: auth,
        isAuthenticated,
        hasFinishedOAuth2,
        isTeacher,
        loginUser,
        logoutUser,
        updateUser,
        removeJwt,
        removeRefresh,
      }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuthContext = () => {
  const authContext = useContext(AuthContext);

  if (!authContext) {
    throw new Error(
      'useAuthContext has to be used within <AuthContext.Provider>'
    );
  }

  return authContext;
};
