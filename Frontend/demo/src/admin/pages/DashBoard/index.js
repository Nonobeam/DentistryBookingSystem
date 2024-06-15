import React from 'react';
import { AppLayout } from '../../core/layout/AppLayout';
import { Outlet } from 'react-router-dom';

export const DashBoard = () => {
  return <AppLayout content={<Outlet />} />;
};
