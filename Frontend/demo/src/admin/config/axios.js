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
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGFtcGxlOUBtYWlsLmNvbSIsImlhdCI6MTcxOTkzMDU5MCwiZXhwIjoxNzIwNTM1MzkwfQ.Vp3ot9UQuT6jwchTz_F9mv3hub7jXhfIh0NXpLv5DHI';
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
export default api;