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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMTBAbWFpbC5jb20iLCJpYXQiOjE3MTk3MjIyMjYsImV4cCI6MTcyMDMyNzAyNn0.7633XBvN7fdrS9JVU9ro7znp5_0fxcNIgFeTKGE15t0';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;