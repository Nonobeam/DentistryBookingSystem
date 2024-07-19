import React from 'react';
import { AppLayout } from '../../admin/core/layout/AppLayout/AppLayout';
import { Outlet } from 'react-router-dom';

export const Pages = () => {
  const role = JSON.stringify(localStorage.getItem('role'));
  const test = JSON.stringify('ADMIN');
  return role === test ? <AppLayout content={<Outlet />} /> : '';
};
