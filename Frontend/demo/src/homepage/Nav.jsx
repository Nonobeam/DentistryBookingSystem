import React, { useEffect, useState } from "react";
import { Layout, Menu, Button, Typography, Dropdown, Drawer } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import {
  HomeOutlined,
  BookOutlined,
  ReadOutlined,
  AppstoreOutlined,
  UserOutlined,
  MenuOutlined,
} from '@ant-design/icons';

const { Header } = Layout;
const { Title } = Typography;

const NavBar = () => {
  const location = useLocation();
  const expirationTime = localStorage.getItem("expirationTime");
  const [visible, setVisible] = useState(false);
  const [isMobile, setIsMobile] = useState(window.innerWidth < 768);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const expirationTime = localStorage.getItem("expirationTime");

    if (!token || new Date().getTime() > expirationTime) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("expirationTime");
    }

    const handleResize = () => setIsMobile(window.innerWidth < 768);
    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const pathToKey = {
    '/': '1',
    '/booking': '2',
    '/educational': '3',
    '/services': '4',
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

  const toggleDrawer = () => {
    setVisible(!visible);
  };

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
        <div className="logo" style={{ flex: '0 0 auto' }}>
          <Link to="/">
            <img
              src={require("../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
              alt="Logo"
              style={{ height: "80px", paddingBottom: "10px"}}
            />
          </Link>
        </div>

        {isMobile ? (
          <>
            <Button
              type="primary"
              icon={<MenuOutlined />}
              onClick={toggleDrawer}
              style={{ marginLeft: "auto" }}
            />
            <Drawer
              title="Menu"
              placement="right"
              onClose={toggleDrawer}
              visible={visible}
              bodyStyle={{ padding: 0 }}
            >
              <Menu
                theme="light"
                mode="vertical"
                selectedKeys={[currentKey]}
                style={{ borderRight: 0 }}
                onClick={toggleDrawer}
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
                {new Date().getTime() < expirationTime ? (
                  menuItems.map(item => (
                    <Menu.Item key={item.key} onClick={item.onClick}>
                      {item.label}
                    </Menu.Item>
                  ))
                ) : (
                  <>
                    <Menu.Item key="login">
                      <Link to="/login">Login</Link>
                    </Menu.Item>
                    <Menu.Item key="signup">
                      <Link to="/signup">Sign up</Link>
                    </Menu.Item>
                  </>
                )}
              </Menu>
            </Drawer>
          </>
        ) : (
          <>
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
          </>
        )}
      </Header>
    </>
  );
};

export default NavBar;
