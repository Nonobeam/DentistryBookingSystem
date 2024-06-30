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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjIiLCJpYXQiOjE3MTk3MzE0NzAsImV4cCI6MTcyMDMzNjI3MH0.Cqy4W0uEJd8UaGXcyy8nshutfWkMT20vJKYfFJTNM1g';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
