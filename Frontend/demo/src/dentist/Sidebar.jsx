import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, ScheduleOutlined, HistoryOutlined, UserOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const { Sider } = Layout;

const Sidebar = () => {
  return (
    <Sider width={250} className="site-layout-background">
      <Menu
        mode="inline"
        defaultSelectedKeys={['1']}
        style={{ height: '100%', borderRight: 0 }}
      >
        <Menu.Item key="1" icon={<HomeOutlined />}>
          <Link to="/dentist">Today's Appointments</Link>
        </Menu.Item>
        <Menu.Item key="2" icon={<ScheduleOutlined />}>
          <Link to="/dentist/schedule">Appointment Schedule</Link>
        </Menu.Item>
        <Menu.Item key="3" icon={<HistoryOutlined />}>
          <Link to="/dentist/history">Appointment History</Link>
        </Menu.Item>
        <Menu.Item key="4" icon={<UserOutlined />}>
          <Link to="/dentist/profile">Profile</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default Sidebar;
