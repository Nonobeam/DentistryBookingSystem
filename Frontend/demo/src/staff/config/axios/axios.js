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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmcyIiwiaWF0IjoxNzE5OTg3OTEzLCJleHAiOjE3MjA1OTI3MTN9.kgINRCmfL7uLwY2l9zjg562azIKFuj9m0Jc6JDAZDV4';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;
