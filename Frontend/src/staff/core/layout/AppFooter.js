import React from 'react';
import { Layout, Row, Col, Typography, Divider } from 'antd';
import { FacebookOutlined, TwitterOutlined, YoutubeOutlined, LinkedinOutlined } from '@ant-design/icons';

const { Title, Paragraph } = Typography;

export const AppFooter=()=> {
    return (
        <Layout.Footer style={{ backgroundColor: '#1890ff', color: 'white', padding: '0' }}>
            <Row gutter={[16, 16]} justify="center">
                <Col xs={24} sm={12} md={8}>
                    <Title level={4} style={{ color: 'white' }}>About Us</Title>
                    <Paragraph style={{ color: 'white' }}>
                        Sunflower Dentistry is dedicated to providing top-notch dental care to our community.
                    </Paragraph>
                </Col>
                <Col xs={24} sm={12} md={8}>
                    <Title level={4} style={{ color: 'white' }}>Contact</Title>
                    <Paragraph style={{ color: 'white' }}>
                        VNUHCM Student Cultural House
                    </Paragraph>
                    <Paragraph style={{ color: 'white' }}>
                        Luu Huu Phuoc, Dong Hoa, Di An, Binh Duong
                    </Paragraph>
                    <Paragraph style={{ color: 'white' }}>
                        dentistrysunflower@gmail.com
                    </Paragraph>
                </Col>
                <Col xs={24} sm={12} md={8}>
                    <Title level={4} style={{ color: 'white' }}>Follow Us</Title>
                    <Paragraph>
                        <FacebookOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                        <TwitterOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                        <YoutubeOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                        <LinkedinOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                    </Paragraph>
                </Col>
            </Row>
            <div style={{ textAlign: 'center', color: 'white', marginTop:20}}>
                <Paragraph style={{ textAlign: 'center', color: '#C6E2FF' }}>
                    &copy; {new Date().getFullYear()} Sunflower Dentistry. All Rights Reserved.
                </Paragraph>
            </div>
        </Layout.Footer>
    );
};

