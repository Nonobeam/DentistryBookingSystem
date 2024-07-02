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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlOUBtYWlsLmNvbSIsImlhdCI6MTcxOTkyMTI5MSwiZXhwIjoxNzIwNTI2MDkxfQ.6wnkFTH7QaYS3SAschGpB2PcB9pMQ3J02QMyeOVTwos ';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;