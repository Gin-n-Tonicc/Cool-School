// Util file that helps us handle initial auth flags

let isInitialAuth = false;

export const initialAuthUtils = {
  hasFinishedInitialAuth() {
    return isInitialAuth === true;
  },
  finishInitialAuth() {
    isInitialAuth = true;
  },
};
