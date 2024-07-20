import React, { useEffect, useState } from "react";
import { Layout, Menu, Button, Dropdown, Drawer } from 'antd';
import { Link, useLocation, useNavigate } from 'react-router-dom';
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
  const navigate = useNavigate();
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
      label: <Link to="/">Home</Link>,
      icon: <HomeOutlined />,
    },
    {
      key: '2',
      label: <Link to="/booking">Booking</Link>,
      icon: <BookOutlined />,
    },
    {
      key: '3',
      label: <Link to="/educational">Educational</Link>,
      icon: <ReadOutlined />,
    },
    {
      key: '4',
      label: <Link to="/services">Services</Link>,
      icon: <AppstoreOutlined />,
    },
  ];

  const accountMenuItems = [
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
              items={menuItems.concat(
                new Date().getTime() < expirationTime ? accountMenuItems : [
                  {
                    key: 'login',
                    label: <Link to="/login">Login</Link>
                  },
                  {
                    key: 'signup',
                    label: <Link to="/signup">Sign up</Link>
                  }
                ]
                )}
              onClick={toggleDrawer}
            />
            </Drawer>
          </>
        ) : (
          <>
          <StyledMenu
              mode="horizontal"
              selectedKeys={[currentKey]}
            items={menuItems}
          />
          <div>
              {new Date().getTime() < expirationTime ? (
              <Dropdown menu={{ items: accountMenuItems }} placement="bottomRight">
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
