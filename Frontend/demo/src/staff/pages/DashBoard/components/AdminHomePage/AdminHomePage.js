import React from 'react';
import { Row, Col, Card } from 'antd';
import {
  CalendarOutlined,
  UserOutlined,
  HistoryOutlined,
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const AdminHomePage = () => {
  const navigate = useNavigate();
  const handleNavigate = (link) => {
    navigate(link);
  };

  return (
    <div style={{ padding: '20px' }}>
      <h1>Welcome, Admin!</h1>
      <Row gutter={16}>
        <Col span={8}>
          <Card
            onClick={() => handleNavigate(' /staff/timetable')}
            hoverable
            cover={
              <img
                alt='Schedule'
                src='https://png.pngtree.com/element_our/png/20180918/dentist-png_100770.jpg'
              />
            }
            style={{ textAlign: 'center' }}>
            <h2>Schedule</h2>
            <CalendarOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
            <p>Manage appointments, schedule, and availability.</p>
          </Card>
        </Col>
        <Col span={8}>
          <Card
            onClick={() => handleNavigate('/staff/customer-list')}
            hoverable
            cover={
              <img
                alt='Patients'
                src='https://png.pngtree.com/element_our/png/20180918/dentist-png_100770.jpg'
              />
            }
            style={{ textAlign: 'center' }}>
            <h2>Customers</h2>
            <UserOutlined style={{ fontSize: '48px', color: '#87d068' }} />
            <p>View and manage patient profiles and records.</p>
          </Card>
        </Col>
        <Col span={8}>
          <Card
            onClick={() => handleNavigate('/staff/appointment-history')}
            hoverable
            cover={
              <img
                alt='History'
                src='https://png.pngtree.com/element_our/png/20180918/dentist-png_100770.jpg'
              />
            }
            style={{ textAlign: 'center' }}>
            <h2>History</h2>
            <HistoryOutlined style={{ fontSize: '48px', color: '#f5222d' }} />
            <p>View appointment history and past treatments.</p>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default AdminHomePage;
