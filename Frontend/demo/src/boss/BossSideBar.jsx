import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, TeamOutlined, BarChartOutlined, UserOutlined } from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';

const { Sider } = Layout;

const BossSidebar = () => {
  const location = useLocation();

  const pathToKey = {
    '/boss': '1',
    '/boss/manager': '2',
    '/boss/service': '3',
    '/boss/profile': '4'
  };

  return (
    <Sider width={250} className="site-layout-background">
      
      <div className="logo" style={{ flex: '0 0 auto', backgroundColor: 'white' }}>
          <Link to="/">
            <img
              src={require("../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
              alt="Logo"
              style={{ height: "120px", paddingBottom: "10px", paddingLeft: "50px"}}
            />
          </Link>
        </div>

      <Menu
        mode="inline"
        defaultSelectedKeys={[pathToKey[location.pathname]]}
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
