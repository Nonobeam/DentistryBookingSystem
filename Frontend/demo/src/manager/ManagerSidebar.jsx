import React from 'react';
import { Layout, Menu } from 'antd';
import { HomeOutlined, TeamOutlined, MedicineBoxOutlined, BarChartOutlined} from '@ant-design/icons';
import { Link, useLocation } from 'react-router-dom';
import { LuLogOut } from 'react-icons/lu';

const { Sider } = Layout;

const ManagerSidebar = () => {
  const location = useLocation();
  const selectedKey = location.pathname.split('/')[2] || '';

  const items = [
    {
      key: '',
      icon: <BarChartOutlined />,
      label: <Link to="/manager">Dashboard</Link>,
    },
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
    },{
      key: 'logout',
      label: 'Logout',
      icon: <LuLogOut />,
      onClick: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('expirationTime');
        window.location.href = '/';
      },
    }
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
