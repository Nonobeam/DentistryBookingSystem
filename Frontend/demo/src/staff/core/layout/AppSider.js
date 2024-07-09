import { Menu } from 'antd';
import React from 'react';
import { Link } from 'react-router-dom';
import { DashboardOutlined, UserOutlined, HistoryOutlined, HomeOutlined, ScheduleOutlined } from '@ant-design/icons'; // Import các icon từ Ant Design

const items = [
  {
    key: 'Dashboard',
    label: 'Dashboard',
    icon: <DashboardOutlined />,
    link: '/staff/dashboard'
  },
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
    key: 'Homepage',
    label: 'Home Page',
    icon: <HomeOutlined />,
    link: ''
  },
  {
    key: 'Timetable',
    label: 'Timetable',
    icon: <ScheduleOutlined />,
    link: '/staff/timetable'
  },
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
          <Menu.Item key={item.key} icon={item.icon} style={{ margin: 0 }}>
            <Link to={item.link} style={{ color: '#333', textDecoration: 'none' }}>
              {item.label}
            </Link>
          </Menu.Item>
        ))}
      </Menu>
    </div>
  );
};
