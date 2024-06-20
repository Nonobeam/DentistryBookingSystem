import React from 'react';
import { Card, Col, Row } from 'antd';
import './CarDash.css';
import UserChart from './ChartUser/UserChart';
const totalUserData = [
  { month: 'Jan', users: 100 },
  { month: 'Feb', users: 150 },
  { month: 'Mar', users: 200 },
  { month: 'Apr', users: 180 },
  { month: 'May', users: 220 },
  { month: 'Jun', users: 250 },
  { month: 'Jul', users: 280 },
  { month: 'Aug', users: 300 },
  { month: 'Sep', users: 320 },
  { month: 'Oct', users: 350 },
  { month: 'Nov', users: 380 },
  { month: 'Dec', users: 400 },
];

export const CarDash = () => (
  <Row gutter={16}>
    <Col span={16}>
    <div>
      <p>khách hàng</p>
      <p style={{ fontSize: '20px', fontWeight: 'bold'}}>782 customer</p>
    </div>
      <UserChart data={totalUserData} />
    </Col>
    <Col span={8}>
      <div class='order-time-chart'>
        <div class='time-slot'>
          <div class='time'>9:00 AM</div>
          <div class='order'>Order 1</div>
        </div>
        <div class='time-slot'>
          <div class='time'>10:00 AM</div>
          <div class='order'>Order 2</div>
        </div>
        <div class='time-slot'>
          <div class='time'>11:00 AM</div>
          <div class='order'>Order 3</div>
        </div>
      </div>
    </Col>
    <Col span={8}>
      <Card title='Card title' bordered={false}>
        Card content
      </Card>
    </Col>
    <Col span={8}>
      <Card title='Card title' bordered={false}>
        Card content
      </Card>
    </Col>
  </Row>
);
