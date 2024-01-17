import Cookies from 'js-cookie';
import {
  AUTH_COOKIE_KEY_JWT,
  AUTH_COOKIE_KEY_REFRESH,
} from '../constants/cookieConstants';

export const getJwtCookie = () => getCookie(AUTH_COOKIE_KEY_JWT);
export const getRefreshCookie = () => getCookie(AUTH_COOKIE_KEY_REFRESH);

export const deleteJwtCookie = () => deleteCookie(AUTH_COOKIE_KEY_JWT);
export const deleteRefreshCookie = () => deleteCookie(AUTH_COOKIE_KEY_REFRESH);

function getCookie(name: string) {
  return Cookies.get(name);
}

function deleteCookie(name: string) {
  return Cookies.remove(name);
}
