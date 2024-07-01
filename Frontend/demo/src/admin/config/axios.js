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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlMTBAbWFpbC5jb20iLCJpYXQiOjE3MTk3MjY0NjUsImV4cCI6MTcyMDMzMTI2NX0.UEcHNzThjJoYXfNsTw-1PkCnd83ZSJF90YvHzg57TFo ';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;