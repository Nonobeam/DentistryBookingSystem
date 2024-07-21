import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';
import {
  DashboardOutlined,
  UserOutlined,
  HistoryOutlined,
  ScheduleOutlined,
} from '@ant-design/icons';

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
      window.location.href = '/';
    },
  },
];

export const AppSider = () => {
  return (
    <div
      style={{
        borderRight: '1px solid #e8e8e8',
        height: '100vh',
        boxShadow: '2px 0 8px rgba(0,0,0,0.15)',
      }}>
      <div
        className='logo'
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '120px',
          backgroundColor: 'white',
        }}>
        <Link to='/staff'>
          <img
            src={require('../../../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png')}
            alt='Logo'
            style={{
              height: '100px',
              objectFit: 'contain',
            }}
          />
        </Link>
      </div>
      <Menu
        mode='inline'
        defaultSelectedKeys={['Dashboard']}
        style={{
          borderRight: 'none',
          backgroundColor: 'transparent',
        }}>
        {items.map((item) => (
          <Menu.Item
            key={item.key}
            icon={React.cloneElement(item.icon, {
              style: { fontSize: '18px', color: '#1976d2' },
            })}
            style={{
              margin: '10px 0',
              borderRadius: '0 10px 10px 0',
              transition: 'all 0.3s',
            }}
            onClick={item.onClick}>
            <Link
              to={item.link}
              style={{
                color: '#333',
                textDecoration: 'none',
                fontSize: '14px',
                fontWeight: 400,
              }}>
              {item.label}
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};