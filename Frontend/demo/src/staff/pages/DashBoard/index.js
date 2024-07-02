import React from 'react';
import { AppLayout } from '../../core/layout/AppLayout';
import { Outlet } from 'react-router-dom';

export const DashBoard = () => {
  const role = JSON.stringify(localStorage.getItem('role'));
  const test = JSON.stringify('STAFF');
  return role === test ? <AppLayout content={<Outlet />} /> : '';
};
