import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';

const items = [
  {
    key: 'Dashboard',
    label: (
      <Link to='/staff' style={{ color: '#333', textDecoration: 'none' }}>
        Dashboard
      </Link>
    ),
  },
  {
    key: 'DentistList',
    label: (
      <Link
        to='/staff/dentist-list'
        style={{ color: '#333', textDecoration: 'none' }}>
        Dentist List
      </Link>
    ),
  },
  {
    key: 'AppointmentHistory',
    label: (
      <Link
        to='/staff/appointment-history'
        style={{ color: '#333', textDecoration: 'none' }}>
        Appointment History
      </Link>
    ),
  },
  {
    key: 'Homepage',
    label: (
      <Link
        to='/staff/admin-home-page'
        style={{ color: '#333', textDecoration: 'none' }}>
        Home Page
      </Link>
    ),
  },
  {
    key: 'Timetable',
    label: (
      <Link
        to='/staff/timetable'
        style={{ color: '#333', textDecoration: 'none' }}>
        Timetable
      </Link>
    ),
  },
];

export const AppSider = () => {
  return (
    <div
      style={{
        backgroundColor: '#fff',
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.15)',
        borderRight: 'none',
        height: '100%',
      }}>
      <div className='demo-logo-vertical' style={{ height: '40px' }} />
      <Menu
        mode='inline'
        defaultSelectedKeys={['cardash']}
        style={{ borderRight: 'none' }}>
        {items.map((item) => (
          <Menu.Item key={item.key} style={{ margin: 0 }}>
            {item.label}
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};
