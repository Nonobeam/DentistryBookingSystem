import React, { useEffect, useState } from "react";
import { Layout, Menu, Button, Typography, Dropdown, Drawer } from 'antd';
import { Link, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import {
  HomeOutlined,
  BookOutlined,
  ReadOutlined,
  AppstoreOutlined,
  UserOutlined,
  MenuOutlined,
} from '@ant-design/icons';

const { Header } = Layout;

const StyledHeader = styled(Header)`
  background: linear-gradient(to right, #1976d2, #1976d2);
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: fixed;
  width: 100%;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
 `;

const Logo = styled(Link)`
  img {
    height: 50px;
    margin-top: 7px;
  }
`;

const StyledMenu = styled(Menu)`
  background: transparent;
  border-bottom: none;
  line-height: 64px;
  flex: 1;
  display: flex;
  justify-content: center;

  .ant-menu-item, .ant-menu-submenu-title {
    color: #e3f2fd;
    font-weight: 500;
    
    &:hover {
      color: #ffffff;
      background-color: rgba(255,255,255,0.1);
    }
    
    &.ant-menu-item-selected {
      color: #ffffff;
      background-color: rgba(255,255,255,0.2);
    }
  }
`;

const StyledButton = styled(Button)`
  background-color: #5fa0e5;
  border-color: #2b6777;
  color: white;
  border-radius: 20px;
  
  &:hover, &:focus {
    background-color: #52ab98;
    border-color: #52ab98;
    color: white;
  }
`;

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
      label: <Link to="/appointment-feedback">Feedback</Link>
    },
    {
      key: '4',
      label: 'Logout',
      onClick: handleLogout
    },
  ];

  const toggleDrawer = () => {
    setVisible(!visible);
  };

  return (
    <StyledHeader>
      <Logo to="/">
            <img
              src={require("../img/z5622999205798_a788dec6bb647bf92381ce26586c370b-removebg.png")}
              alt="Logo"
              style={{ height: "100px", paddingBottom: "20px" }}
              href="/"
            />
      </Logo>

        {isMobile ? (
          <>
          <StyledButton
              icon={<MenuOutlined />}
              onClick={toggleDrawer}
            />
            <Drawer
              title="Menu"
              placement="right"
              onClose={toggleDrawer}
            open={visible}
              bodyStyle={{ padding: 0 }}
            >
            <StyledMenu
                mode="vertical"
                selectedKeys={[currentKey]}
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
            </StyledMenu>
            </Drawer>
          </>
        ) : (
          <>
          <StyledMenu
              mode="horizontal"
              selectedKeys={[currentKey]}
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
          </StyledMenu>

          <div>
              {new Date().getTime() < expirationTime ? (
                <Dropdown menu={{ items: menuItems }} placement="bottomRight">
                <StyledButton icon={<UserOutlined />}>My Account</StyledButton>
                </Dropdown>
              ) : (
                <>
                <StyledButton style={{ marginRight: 10 }}>
                    <Link to="/login">Login</Link>
                </StyledButton>
                <StyledButton>
                  <Link to="/signup">Sign up</Link>
                </StyledButton>
                </>
              )}
            </div>
          </>
        )}
    </StyledHeader>
  );
};

export default NavBar;