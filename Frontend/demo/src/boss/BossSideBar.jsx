import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, TeamOutlined, BarChartOutlined, UserOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';

const { Sider } = Layout;

const BossSidebar = () => {
  return (
    <Sider width={250} className="site-layout-background">
      <Menu
        mode="inline"
        defaultSelectedKeys={['1']}
        style={{ height: '100%', borderRight: 0 }}
        items={[
            {
                key: '1',
                icon: <BarChartOutlined />,
                label: <Link to="/boss">Dashboard</Link>,
              },
          
          {
            key: '2',
            icon: <TeamOutlined />,
            label: <Link to="/boss/manager">Manager List</Link>,
          },
          {
            key: '3',
            icon: <HomeOutlined />,
            label: <Link to="/boss/service">Service</Link>,
          },
          {
            key: '4',
            icon: <UserOutlined />,
            label: <Link to="/boss/profile">Profile</Link>,
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
        ]}
      />
    </Sider>
  );
};

export default BossSidebar;
