import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';
import {
  DashboardOutlined,
  UserOutlined,
  HistoryOutlined,
  HomeOutlined,
  ScheduleOutlined,
} from '@ant-design/icons'; // Import các icon từ Ant Design

const items = [
  {
    key: 'Dashboard',
    label: 'Dashboard',
    icon: <DashboardOutlined />,
    link: '/staff',
  },
  {
    key: 'DentistList',
    label: 'Dentist List',
    icon: <UserOutlined />,
    link: '/staff/dentist-list',
  },
  {
    key: 'CustomerList',
    label: 'Customer List',
    icon: <UserOutlined />,
    link: '/staff/customer-list',
  },
  {
    key: 'AppointmentHistory',
    label: 'Appointment History',
    icon: <HistoryOutlined />,
    link: '/staff/appointment-history',
  },
  {
    key: 'Timetable',
    label: 'Timetable',
    icon: <ScheduleOutlined />,
    link: '/staff/timetable',
  },
  {
    key: 'Profile',
    label: 'Profile',
    icon: <UserOutlined />,
    link: '/staff/profile',
  },
  {
    key: 'Logout',

    label: 'Logout',
    icon: <LuLogOut />,
    onClick: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('expirationTime');
      window.location.href = '/'; // Redirect to the homepage after logout
    },
  },
];

export const AppSider = () => {
  return (
    <div
      style={{
        backgroundColor: '#fff',
        borderRight: 'none',
        height: '100vh',
      }}>
      <div className='demo-logo-vertical' style={{ height: '0px' }} />
      <div
        className='logo'
        style={{ flex: '0 0 auto', backgroundColor: 'white' }}>
        <Link to='/'>
          <img
            src={require('../../../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png')}
            alt='Logo'
            style={{
              height: '130px',
              paddingBottom: '10px',
              paddingLeft: '40px',
            }}
          />
        </Link>
      </div>
      <Menu
        mode='inline'
        defaultSelectedKeys={['Dashboard']}
        style={{ borderRight: 'none' }}>
        {items.map((item) => (
          <Menu.Item
            key={item.key}
            icon={item.icon}
            style={{ margin: 0 }}
            onClick={item.onClick}>
            <Link
              to={item.link}
              style={{ color: '#333', textDecoration: 'none' }}>
              {item.label}
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};
