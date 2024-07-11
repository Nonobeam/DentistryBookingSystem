import React, { useEffect } from "react";
import { Layout, Menu, Button, Typography, Dropdown } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import {
  HomeOutlined,
  BookOutlined,
  ReadOutlined,
  AppstoreOutlined,
  UserOutlined,
} from '@ant-design/icons';

const { Header } = Layout;
const { Title } = Typography;

const NavBar = () => {
  const location = useLocation();
  const expirationTime = localStorage.getItem("expirationTime");

  useEffect(() => {
    const token = localStorage.getItem("token");
    const expirationTime = localStorage.getItem("expirationTime");
    
    if (!token || new Date().getTime() > expirationTime) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("expirationTime");
    }
  }, []);

  const pathToKey = {
    '/': '1',
    '/booking': '2',
    '/educational': '3',
    '/services': '4'
  };
  const currentKey = pathToKey[location.pathname];

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("expirationTime");
    window.location.href = '/'; // Redirect to the homepage after logout
  };

  const menuItems = [
    {
      key: '1',
      label: <Link to="/history">Appointment History</Link>
    },
    {
      key: '2',
      label: <Link to="/profile">Profile</Link>
    },
    {
      key: '3',
      label: 'Logout',
      onClick: handleLogout
    },
  ];

  return (
    <>
      <Header
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          backgroundColor: "#1890ff",
          padding: "0 20px"
        }}
      >
        <div className="logo">
          <Title level={4} style={{ color: "white", margin: 0 }}>
            Sunflower Dentistry
          </Title>
        </div>

        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[currentKey]}
          style={{
            flex: 1,
            justifyContent: "center",
            backgroundColor: "#1890ff",
            borderBottom: "none"
          }}
        >
          <Menu.Item key="1" icon={<HomeOutlined />}>
            <Link to="/">Home</Link>
          </Menu.Item>
          <Menu.Item key="2" icon={<BookOutlined />}>
            <Link to="/booking">Booking</Link>
          </Menu.Item>
          <Menu.Item key="3" icon={<ReadOutlined />}>
            <Link to="/educational">Educational</Link>
          </Menu.Item>
          <Menu.Item key="4" icon={<AppstoreOutlined />}>
            <Link to="/services">Services</Link>
          </Menu.Item>
        </Menu>

        <div
          style={{ display: "flex", alignItems: "center", marginLeft: "auto" }}
        >
          {new Date().getTime() < expirationTime ? (
            <Dropdown menu={{ items: menuItems }} placement="bottomRight">
              <Button icon={<UserOutlined />}>My Account</Button>
            </Dropdown>
          ) : (
            <>
              <Button type="primary" style={{ marginRight: "10px" }}>
                <Link to="/login">Login</Link>
              </Button>
              <Button><Link to="/signup">Sign up</Link></Button>
            </>
          )}
        </div>
      </Header>
    </>
  );
};

export default NavBar;
