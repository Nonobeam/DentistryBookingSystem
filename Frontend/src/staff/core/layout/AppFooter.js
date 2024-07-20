import React from 'react';
import { Layout, Row, Col, Typography } from 'antd';

const { Title, Paragraph } = Typography;

export const AppFooter=()=> {
    return (
        <Layout.Footer style={{ backgroundColor: '#1976d2', color: 'white', padding: '0' }}>
<Row justify="center" style={{ marginTop: '0px' }}>
      <Col>
        <Paragraph style={{ color: 'white' }}>
          © 2024 Sunflower Dentistry. All rights reserved.
        </Paragraph>
      </Col>
    </Row>
        </Layout.Footer>
    );
};

