import { PropsWithChildren, createContext, useContext } from 'react';
import { useLocalStorage } from '../hooks/useLocalStorage';
import { RolesEnum } from '../types/enums/RolesEnum';
import { IAuthResponse } from '../types/interfaces/IAuthResponse';
import { IAuthStorage } from '../types/interfaces/IAuthStorage';
import { IUser } from '../types/interfaces/IUser';
import { isJwtExpired } from '../utils/jwtUtils';

type AuthContextType = {
  user: Partial<IAuthStorage>;
  isAuthenticated: boolean;
  isTeacher: boolean;
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
      id: user.id,
      username: user.username,
      firstname: user.firstname,
      role: user.role,
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

  const isTeacher = auth.role === RolesEnum.TEACHER;

  return (
    <AuthContext.Provider
      value={{
        user: auth,
        isAuthenticated,
        isTeacher,
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
