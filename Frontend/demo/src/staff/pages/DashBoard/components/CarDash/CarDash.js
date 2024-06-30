import React, { useEffect, useState } from 'react';
import { Card, Col, Row } from 'antd';
import { ChartDailyAppointment } from './ChartDailyAppointment/ChartDailyAppointment';
import { ChartMonthly } from './ChartMonthly/ChartMonthly';
import { DashBoardServices } from '../../../../services/DashBoardServices/DashBoardServices';
const data = {
  date: 'new Date().toISOString().slice(0, 10)',
  year: new Date().getFullYear(),
};

export const CarDash = () => {
  console.log(data);
  const [dashboardData, setDashboardData] = useState({});
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await DashBoardServices.getAll(data);
        console.log(response);
        // setDashboardData(response || {}); // Đảm bảo response không null
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      }
    };
    fetchData();
  }, []);

  // Kiểm tra và khởi tạo dữ liệu mặc định nếu không có
  const dailyAppointmentsData = {
    labels: Object.keys(dashboardData.dailyAppointments || {}),
    datasets: [
      {
        label: 'Daily Appointments',
        data: Object.values(dashboardData.dailyAppointments || {}),
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
        ],
      },
    ],
  };

  const monthlyAppointmentsData = {
    labels: Object.keys(dashboardData.monthlyAppointments || {}).map(
      (month) => `Month ${month}`
    ),
    datasets: [
      {
        label: 'Monthly Appointments',
        data: Object.values(dashboardData.monthlyAppointments || {}),
        backgroundColor: '#8884d8',
      },
    ],
  };

  return (
    <Row gutter={16}>
      <Col span={16}>
        <ChartDailyAppointment dailyAppointmentsData={dailyAppointmentsData} />
      </Col>
      <Col span={8}>
        <ChartMonthly monthlyAppointmentsData={monthlyAppointmentsData} />
      </Col>
      <Col span={8}>
        <Card
          title='Total Appointments Now'
          bordered={false}
          style={{ marginBottom: '16px' }}>
          {dashboardData.totalAppointmentsInMonthNow || 0}{' '}
          {/* Kiểm tra và hiển thị giá trị, mặc định là 0 nếu không có */}
        </Card>
      </Col>
      <Col span={8}>
        <Card
          title='Total Appointments In Year Now'
          bordered={false}
          style={{ marginBottom: '16px' }}>
          {dashboardData.totalAppointmentsInYearNow || 0}{' '}
          {/* Kiểm tra và hiển thị giá trị, mặc định là 0 nếu không có */}
        </Card>
      </Col>
    </Row>
  );
};
