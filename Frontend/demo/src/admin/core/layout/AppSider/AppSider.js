import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';

const items = [
  {
    key: 'CustomerList',
    label: (
      <Link to='/admin/customer-list-admin' style={{ color: '#333', textDecoration: 'none' }}>
        CustomerList
      </Link>
    ),
  },
  {
    key: 'DentistList',
    label: (
      <Link
        to='/admin/dentist-list-admin'
        style={{ color: '#333', textDecoration: 'none' }}>
        DentistList
      </Link>
    ),
  },
  
  {
    key: 'StaffList',
    label: (
      <Link
        to='/admin/staff-list'
        style={{ color: '#333', textDecoration: 'none' }}>
        StaffList
      </Link>
    ),
  },
  {
    key: 'ManagerList',
    label: (
      <Link
        to='/admin/manager-list'
        style={{ color: '#333', textDecoration: 'none' }}>
        ManagerList
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
        height: '100vh',
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
