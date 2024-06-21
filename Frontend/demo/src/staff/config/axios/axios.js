import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api/v1/', // Replace with your actual base URL
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
    Authorization:
      'Bearer ' +
      'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdGFmZjIiLCJpYXQiOjE3MTg5MzkwMDMsImV4cCI6MTcxODk0MDQ0M30.U-rLrNeYGx-B6H9d-cHFyie9LYIiybUoW8MEymlFvpU',
  },
});

export default api;
