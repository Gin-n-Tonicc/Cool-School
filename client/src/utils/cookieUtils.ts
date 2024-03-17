// Util file that helps us handle cookies

import Cookies from 'js-cookie';

const AUTH_COOKIE_KEY_JWT = 'COOL_SCHOOL_SESSION_JWT';
const AUTH_COOKIE_KEY_REFRESH = 'COOL_SCHOOL_SESSION_REFRESH';

export const deleteJwtCookie = () => deleteCookie(AUTH_COOKIE_KEY_JWT);
export const deleteRefreshCookie = () => deleteCookie(AUTH_COOKIE_KEY_REFRESH);

function deleteCookie(name: string) {
  return Cookies.remove(name);
}
