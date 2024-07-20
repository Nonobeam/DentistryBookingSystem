import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, ScheduleOutlined, HistoryOutlined, UserOutlined } from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';

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
    {
      key: '5',
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
      <div className="logo" style={{ flex: '0 0 auto', backgroundColor: 'white' }}>
        <Link to="/">
          <img
            src={require("../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
            alt="Logo"
            style={{ height: "120px", paddingBottom: "10px", paddingLeft: "50px" }}
          />
        </Link>
      </div>
      <Menu
        mode="inline"
        selectedKeys={[getDefaultSelectedKey()]}
        style={{ height: '100%', borderRight: 0 }}
      >
        {menuItems.map(item => (
          <Menu.Item
            key={item.key}
            icon={React.cloneElement(item.icon, { style: { fontSize: '18px', color: '#1976d2' } })}
            onClick={item.onClick}
            style={{ margin: '10px 0', borderRadius: '0 20px 20px 0', transition: 'all 0.3s' }}
          >
            {item.label}
          </Menu.Item>
        ))}
      </Menu>
    </Sider>
  );
};

export default Sidebar;
