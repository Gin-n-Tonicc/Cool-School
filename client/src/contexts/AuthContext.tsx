import { PropsWithChildren, createContext, useContext } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { IAuthResponse } from '../interfaces/IAuthResponse';
import { IAuthStorage } from '../interfaces/IAuthStorage';
import { IUser } from '../interfaces/IUser';
import { isJwtExpired } from '../utils/jwtUtils';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  updateUser: (v: IUser) => void;
  loginUser: (v: IAuthResponse) => void;
  logoutUser: () => void;
  setAccessToken: (token: string | undefined) => void;
  removeRefreshToken: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: PropsWithChildren) {
  const { value: auth, setStorageData: setAuth } =
    useLocalStorage<IAuthStorage>('auth', {});

  const updateUser: AuthContextType['updateUser'] = (object) => {
    setAuth((oldUser) => ({ ...oldUser, ...object }));
  };

  const loginUser: AuthContextType['loginUser'] = ({
    accessToken,
    refreshToken,
    user,
  }) => {
    setAuth({
      accessToken,
      refreshToken,
      email: user.email,
      _id: user._id,
      username: user.username,
      firstname: user.firstname,
    });
  };

  const logoutUser: AuthContextType['logoutUser'] = () => {
    setAuth({});
  };

  const setAccessToken: AuthContextType['setAccessToken'] = (
    token: string | undefined
  ) => {
    setAuth((auth) => ({ ...auth, accessToken: token }));
  };

  const removeRefreshToken: AuthContextType['removeRefreshToken'] = () => {
    setAuth((auth) => ({ ...auth, refreshToken: undefined }));
  };

  const isAuthenticated =
    Boolean(auth.accessToken) && !isJwtExpired(auth.accessToken);

  return (
    <AuthContext.Provider
      value={{
        user: auth,
        isAuthenticated,
        loginUser,
        logoutUser,
        updateUser,
        setAccessToken,
        removeRefreshToken,
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
