import axios from 'axios';

// const token = localStorage.getItem('token');

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1/', // Replace with your actual base URL
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
});
api.interceptors.request.use((config) => {
  const token =
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmcyIiwiaWF0IjoxNzIwMDgxNzMyLCJleHAiOjE3MjA2ODY1MzJ9.6REBdetsLNucucVN4cEd8T7V9lhn5cF4NiUS0glWsTI';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
