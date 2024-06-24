import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1/', // Replace with your actual base URL
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
    Authorization:
      'Bearer ' +
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjIiLCJpYXQiOjE3MTkyMTk2MzcsImV4cCI6MTcxOTgyNDQzN30.hgf8W_rIVm6yNKeYHc5Z_UJOqujjZA0kcQreknPV6rw',
  },
});

export default api;
