import api from '../config/axiosConfig';

export const getHome = () => api.get('/api/home');
