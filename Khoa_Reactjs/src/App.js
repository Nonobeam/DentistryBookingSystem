import React, { useState } from 'react';
import {
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  UploadOutlined,
  UserOutlined,
  VideoCameraOutlined,
} from '@ant-design/icons';
import { Avatar, Button, Layout, Menu, theme } from 'antd';
import { ProfileSettings } from './components/ProfileSettings';
import { UserList } from './pages/UserList';
import { TimeTable } from './pages/TimeTable';
const { Header, Sider, Content } = Layout;

const items = [
  {
    key: '1',
    icon: <UserOutlined />,
    label: 'Users List',
  },
  {
    key: '2',
    icon: <VideoCameraOutlined />,
    label: 'Dentist List',
  },
  {
    key: '3',
    icon: <UploadOutlined />,
    label: 'Statics',
  },
  {
    key: '4',
    icon: <UploadOutlined />,
    label: 'Statics',
  },
];

function App() {
  const [collapsed, setCollapsed] = useState(false);
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();

  const [selectedMenuItem, setSelectedMenuItem] = useState('1');

  const handleMenuClick = ({ key }) => {
    setSelectedMenuItem(key);
  };

  const renderContent = () => {
    switch (selectedMenuItem) {
      case '1':
        return <UserList />;
      case '2':
        return <TimeTable />;
      case '3':
        return <div>Statistics Content</div>;
      case '4':
        return <div>More Statistics Content</div>;
      default:
        return <div>Default Content</div>;
    }
  };
  return (
    <Layout style={{ height: '100vh' }}>
      <Sider style={{ padding: '1rem 1.2rem' }} trigger={null} collapsible collapsed={collapsed}>
        <div className='demo-logo-vertical' />
        <Menu
          padding={'4rem'}
          theme='dark'
          mode='inline'
          defaultSelectedKeys={['1']}
          onClick={handleMenuClick}
          items={items}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            padding: 0,
            background: colorBgContainer,
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
          }}
        >
          <Button
            type='text'
            icon={collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
            onClick={() => setCollapsed(!collapsed)}
            style={{
              fontSize: '16px',
              width: 64,
              height: 64,
            }}
          />
          <ProfileSettings />
        </Header>
        <Content
          style={{
            margin: '24px 16px',
            padding: 24,
            minHeight: 280,
            background: colorBgContainer,
            borderRadius: borderRadiusLG,
          }}
        >
          {renderContent()}
        </Content>
      </Layout>
    </Layout>
  );
}

export default App;