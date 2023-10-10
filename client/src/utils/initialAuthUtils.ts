let isInitialAuth = false;

export const initialAuthUtils = {
  hasFinishedInitialAuth() {
    return isInitialAuth === true;
  },
  finishInitialAuth() {
    isInitialAuth = true;
  },
};
