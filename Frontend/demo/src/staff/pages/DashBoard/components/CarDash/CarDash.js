import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Table } from 'antd';
// import './CarDash.css';
import { ChartDailyAppointment } from './ChartDailyAppointment/ChartDailyAppointment';
import { dashboardData } from '../../../../utils/data';
import { ChartMonthly } from './ChartMonthly/ChartMonthly';
import { DashBoardServices } from '../../../../services/DashBoardServices/DashBoardServices';
import { Chart, ArcElement, linear } from 'chart.js';
import { CategoryScale } from 'chart.js';

export const CarDash = () => {
  // const [dashboardData, setDashboardData] = useState({});

  // useEffect(() => {
  //   const fetchData = async () => {
  //     const response = await DashBoardServices.getAll();
  //     setDashboardData(response);
  //   };
  //   fetchData();
  // }, []);

  const dailyAppointmentsData = {
    labels: Object.keys(dashboardData.dailyAppointments),
    datasets: [
      {
        label: 'Daily Appointments',
        data: Object.values(dashboardData.dailyAppointments),
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56',
          '#4BC0C0',
          '#9966FF',
        ], // Define colors as needed
      },
    ],
  };

  const monthlyAppointmentsData = {
    labels: Object.keys(dashboardData.monthlyAppointments).map(
      (month) => `Month ${month}`
    ),
    datasets: [
      {
        label: 'Monthly Appointments',
        data: Object.values(dashboardData.monthlyAppointments),
        backgroundColor: '#8884d8', // Bar color
      },
    ],
  };
  const servicesUsageData = dashboardData.servicesUsage || []; 

  return (
    <Row gutter={16}>
      <Col span={16}>
        <div>
          {/* <p style={{ marginBottom: '8px' }}>Khách hàng</p>
          <p
            style={{
              fontSize: '20px',
              fontWeight: 'bold',
              marginBottom: '16px',
            }}>
            782 customer
          </p> */}
        </div>
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
          {dashboardData.totalAppointmentsInMonthNow}
        </Card>
      </Col>
      <Col span={8}>
        <Card
          title='Total Appointments In Year Now'
          bordered={false}
          style={{ marginBottom: '16px' }}>
          {dashboardData.totalAppointmentsInYearNow}
        </Card>
      </Col>
    </Row>
  );
};
