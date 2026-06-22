import api from './api';
import type { HomeDto } from '../types';

export const getHome = () => api.get<HomeDto>('/api/home');
