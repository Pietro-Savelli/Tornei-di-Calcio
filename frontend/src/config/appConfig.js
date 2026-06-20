export const APP = (typeof window !== 'undefined' && window.__APP__) || {
  authenticated: false, isAdmin: false, username: null, csrf: { param: '_csrf', token: '' },
};
