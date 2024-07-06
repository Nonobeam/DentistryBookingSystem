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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmcyIiwiaWF0IjoxNzIwMjMwMjI1LCJleHAiOjE3MjA4MzUwMjV9.VGPebVndP9uoZZivT18hnJJg7vbPg_CKlWnXLQNUYqw';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
