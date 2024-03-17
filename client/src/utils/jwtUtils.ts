// Util file that helps us handle json web tokens

const parseJwt = (token: string): { exp: number } | null => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (e) {
    return null;
  }
};

export const isJwtExpired = (token: string | undefined | null) => {
  if (!token) {
    return true;
  }

  const decodedJwt = parseJwt(token);

  if (!decodedJwt || decodedJwt.exp * 1000 < Date.now()) {
    return true;
  }

  return false;
};
