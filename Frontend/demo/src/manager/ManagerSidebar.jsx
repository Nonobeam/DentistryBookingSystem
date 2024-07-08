import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, UserOutlined, TeamOutlined, MedicineBoxOutlined } from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';

const { Sider } = Layout;

const ManagerSidebar = () => {
  const location = useLocation();
  const selectedKey = location.pathname.split('/')[2] || 'staff';

  const items = [
    {
      key: 'staff',
      icon: <TeamOutlined />,
      label: <Link to="/manager/staff">Staff List</Link>,
    },
    {
      key: 'dentist',
      icon: <MedicineBoxOutlined />,
      label: <Link to="/manager/dentist">Dentist List</Link>,
    },
    {
      key: 'clinic',
      icon: <HomeOutlined />,
      label: <Link to="/manager/clinic">Clinic List</Link>,
    },
  ];

  return (
    <Sider width={250} className="site-layout-background">
      <Menu
        mode="inline"
        defaultSelectedKeys={[selectedKey]}
        selectedKeys={[selectedKey]}
        style={{ height: '100%', borderRight: 0 }}
        items={items}
      />
    </Sider>
  );
};

export default ManagerSidebar;
