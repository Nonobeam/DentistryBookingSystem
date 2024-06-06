import React from "react";
import { Layout, Menu, Button, Typography } from 'antd';
import { Link, useLocation } from 'react-router-dom';

const { Header } = Layout;
const { Title } = Typography;

const NavBar = () => {
  const location = useLocation();
  const pathToKey = {
    '/': '1',
    '/booking': '2',
    '/educational': '3',
    '/services': '4'
  };
  const currentKey = pathToKey[location.pathname];


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
          > 
            <Menu.Item key="1"><Link to="/">Home</Link></Menu.Item>
            <Menu.Item key="2"><Link to="/booking">Booking</Link></Menu.Item>
            <Menu.Item key="3"><Link to="/educational">Educational</Link></Menu.Item>
            <Menu.Item key="4"><Link to="/services">Services</Link></Menu.Item>
          </Menu>
        </div>

        <div
          style={{ display: "flex", alignItems: "center", marginLeft: "auto" }}
        >
          <Button type="primary" style={{ marginRight: "10px" }}>
            <Link to="/login">Login</Link>
          </Button>
          <Button><Link to="/signup">Sign up</Link></Button>
        </div>
      </Header>
    );
};

export default NavBar;