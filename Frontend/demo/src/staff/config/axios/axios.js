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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjIiLCJpYXQiOjE3MTk3NDg0ODgsImV4cCI6MTcyMDM1MzI4OH0.ifeebEVPJJeq5VFA0ZE3z-nacvVjUojKr4uHa6ui8UY';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
