import React, { useEffect } from 'react';
import { AppLayout } from '../../core/layout/AppLayout';
import { Outlet } from 'react-router-dom';
export const DashBoard = () => {
  const expirationTime = localStorage.getItem('expirationTime');

  useEffect(() => {
    const token = localStorage.getItem('token');
    const expirationTime = localStorage.getItem('expirationTime');

    if (!token || new Date().getTime() > expirationTime) {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('expirationTime');
    }
  }, []);
  const role = JSON.stringify(localStorage.getItem('role'));
  const test = JSON.stringify('STAFF');
  return role === test ? <AppLayout content={<Outlet />} /> : '';
};
