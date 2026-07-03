import axios from 'axios';

// Swapped out local dev configurations for your live Render gateway url
const API_BASE_URL = 'https://healthbridge-backend.onrender.com/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Outbound Request Interceptor for automatic JWT appending
api.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('hb_user'));
    if (user && user.token) {
      config.headers['Authorization'] = `Bearer ${user.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default api;