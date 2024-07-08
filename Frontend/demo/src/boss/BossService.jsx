import React from 'react';
import { Layout } from 'antd';
import BossSidebar from './BossSideBar';

const { Content } = Layout;

const BossService = () => {
  return (
    <>
      <BossSidebar />
      <Layout style={{ padding: '24px 24px' }}>
        <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
          <h2>Service</h2>
        </Content>
      </Layout>
    </>
  );
};

export default BossService;
