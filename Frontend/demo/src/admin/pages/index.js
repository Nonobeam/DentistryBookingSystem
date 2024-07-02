import React from 'react';
import { AppLayout } from '../../admin/core/layout/AppLayout/AppLayout';
import { Outlet } from 'react-router-dom';

export const Pages = () => {
  return <AppLayout content={<Outlet />} />;
};
