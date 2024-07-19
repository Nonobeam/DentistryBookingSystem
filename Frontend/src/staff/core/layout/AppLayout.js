import { Layout } from 'antd';
import Sider from 'antd/es/layout/Sider';
import { Content, Header } from 'antd/es/layout/layout';
import React from 'react';
import { AppHeader } from './AppHeader'; // Adjust import if needed
import { AppSider } from './AppSider'; // Adjust import if needed
import { AppFooter } from './AppFooter'; // Adjust import if needed

const headerStyle = {
  textAlign: 'center',
  color: '#fff',
  lineHeight: '64px',
  backgroundColor: '#1890ff',
};

const siderStyle = {
  lineHeight: '120px',
  color: '#333',
  backgroundColor: '#fff',
};

export const AppLayout = ({ content }) => {
  return (
    <Layout>
      <Sider
        breakpoint='lg'
        collapsedWidth='0'
        onBreakpoint={(broken) => {
          console.log(broken);
        }}
        onCollapse={(collapsed, type) => {
          console.log(collapsed, type);
        }}
        style={siderStyle}
        width='15%'
      >
        <AppSider />
      </Sider>
      <Layout>
        <Header style={headerStyle}>
          <AppHeader />
        </Header>
        <Content
          style={{
            padding: '0 24px',
            minHeight: 280,
            backgroundColor: '#F5F5F5',
          }}
        >
          {content}
        </Content>
        <Layout.Footer
          style={{
            textAlign: 'center',
            backgroundColor: '#1890ff',
            color: '#fff',
          }}
        >
          <AppFooter />
        </Layout.Footer>
      </Layout>
    </Layout>
  );
};
