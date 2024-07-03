import React, { useEffect } from "react";
import { Layout, Menu, Button, Typography, Dropdown } from 'antd';
import { Link, useLocation } from 'react-router-dom';

const { Header } = Layout;
const { Title } = Typography;

const NavBar = () => {
  const location = useLocation();
  
  useEffect(() => {
    const token = localStorage.getItem("token");
    const expirationTime = localStorage.getItem("expirationTime");

    if (!token || new Date().getTime() > expirationTime) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("expirationTime");
    }
  }, []);

  const token = localStorage.getItem("token");

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
    <Header
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <div className="logo">
        <Title level={4} style={{ color: "white", margin: 0 }}>
          Sunflower Dentistry
        </Title>
      </div>

      <div
        style={{
          display: "flex",
          flex: 1,
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[currentKey]}
          style={{
            display: "flex",
            justifyContent: "center",
            lineHeight: "40px",
            width: "100%"
          }}
          items={[
            { key: '1', label: <Link to="/">Home</Link> },
            { key: '2', label: <Link to="/booking">Booking</Link> },
            { key: '3', label: <Link to="/educational">Educational</Link> },
            { key: '4', label: <Link to="/services">Services</Link> }
          ]}
        />
      </div>

      <div
        style={{ display: "flex", alignItems: "center", marginLeft: "auto" }}
      >
        {token ? (
          <Dropdown menu={{ items: menuItems }} placement="bottomRight">
            <Button>My Account</Button>
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
  );
};

export default NavBar;
