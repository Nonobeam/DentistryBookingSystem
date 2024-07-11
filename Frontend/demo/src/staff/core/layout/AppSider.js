import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';
import { DashboardOutlined, UserOutlined, HistoryOutlined, HomeOutlined, ScheduleOutlined } from '@ant-design/icons'; // Import các icon từ Ant Design

const items = [
  {
    key: 'DentistList',
    label: 'Dentist List',
    icon: <UserOutlined />,
    link: '/staff/dentist-list'
  },
  {
    key: 'CustomerList',
    label: 'Customer List',
    icon: <UserOutlined />,
    link: '/staff/customer-list'
  },
  {
    key: 'AppointmentHistory',
    label: 'Appointment History',
    icon: <HistoryOutlined />,
    link: '/staff/appointment-history'
  },
  {
    key: 'Dasboard',
    label: 'Dash board',
    icon: <DashboardOutlined />,
    link: ''
  },
  {
    key: 'Timetable',
    label: 'Timetable',
    icon: <ScheduleOutlined />,
    link: '/staff/timetable'
  },{
    key: 'profile',
    icon: <UserOutlined />,
    label: <Link to="/staff/profile">Profile</Link>,
  },{
    key: 'logout',
    label: 'Logout',
    icon: <LuLogOut />,
    onClick: () => {
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('expirationTime');
      window.location.href = '/';
    },
  }
];

export const AppSider = () => {
  return (
    <div
      style={{
        backgroundColor: '#fff',
        borderRight: 'none',
        height: '100vh',
      }}
    >
      <div className='demo-logo-vertical' style={{ height: '40px' }} />
      <Menu
        mode='inline'
        defaultSelectedKeys={['Dashboard']}
        style={{ borderRight: 'none' }}
      >
        {items.map((item) => (
          <Menu.Item key={item.key} icon={item.icon} style={{ margin: 0 }} onClick={item.onClick}>
            <Link to={item.link} style={{ color: '#333', textDecoration: 'none' }}>
              {item.label}
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};
