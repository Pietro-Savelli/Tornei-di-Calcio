import api from './api';
import { HomeDto } from '../types';

export const getHome = () => api.get<HomeDto>('/api/home');
