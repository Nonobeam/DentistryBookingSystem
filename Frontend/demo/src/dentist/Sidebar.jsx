import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, ScheduleOutlined, HistoryOutlined, UserOutlined } from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';

const { Sider } = Layout;

const Sidebar = () => {
  const location = useLocation();

  const menuItems = [
    {
      key: '1',
      icon: <HomeOutlined />,
      label: <Link to="/dentist">Today's Appointments</Link>,
    },
    {
      key: '2',
      icon: <ScheduleOutlined />,
      label: <Link to="/dentist/schedule">Appointment Schedule</Link>,
    },
    {
      key: '3',
      icon: <HistoryOutlined />,
      label: <Link to="/dentist/history">Appointment History</Link>,
    },
    {
      key: '4',
      icon: <UserOutlined />,
      label: <Link to="/dentist/profile">Profile</Link>,
    },
  ];

  // Determine the default selected key based on the current path
  const getDefaultSelectedKey = () => {
    switch (location.pathname) {
      case '/dentist/schedule':
        return '2';
      case '/dentist/history':
        return '3';
      case '/dentist/profile':
        return '4';
      default:
        return '1';
    }
  };

  return (
    <Sider width={250} className="site-layout-background">
      <Menu
        mode="inline"
        selectedKeys={[getDefaultSelectedKey()]}
        style={{ height: '100%', borderRight: 0 }}
        items={menuItems}
      />
    </Sider>
  );
};

export default Sidebar;
