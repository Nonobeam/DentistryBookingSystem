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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjIiLCJpYXQiOjE3MTk3MTc5NjQsImV4cCI6MTcyMDMyMjc2NH0.jaFV7sBKt1Fir0qb-HfPF8p9uuPYNOF7xv-vVgLIlEU';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
