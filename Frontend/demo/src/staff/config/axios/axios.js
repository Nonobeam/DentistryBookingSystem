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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjMiLCJpYXQiOjE3MTk5MjI0OTYsImV4cCI6MTcyMDUyNzI5Nn0.Vt9XqopQ8HrcIfW8zuVUh4pdguxE8Gpktyoz2P_Of2U';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
