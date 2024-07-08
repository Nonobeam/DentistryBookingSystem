import React, { useState, useEffect } from 'react';
import { Layout, Select, Card, Row, Col, Typography, Divider } from 'antd';
import axios from 'axios';
import { Bar } from '@ant-design/charts';
import ManagerSidebar from './ManagerSidebar';

const { Content } = Layout;
const { Option } = Select;
const { Title } = Typography;

const ManagerDashboard = () => {
  const [year, setYear] = useState(2024);
  const [dashboardData, setDashboardData] = useState({
    dailyAppointments: 0,
    monthlyAppointments: {},
    totalAppointmentsInMonthNow: 0,
    totalAppointmentsInYearNow: 0,
  });

  useEffect(() => {
    fetchDashboardData(year);
  }, [year]);

  const fetchDashboardData = async (year) => {
    const token = localStorage.getItem('token');
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/manager/dashboard?year=${year}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = response.data;
      // Set dailyAppointments to 0 if it's null
      if (data.dailyAppointments === null) {
        data.dailyAppointments = 0;
      }
      setDashboardData(data);
    } catch (error) {
      console.error('There was an error fetching the dashboard data!', error);
    }
  };

  const handleYearChange = (value) => {
    setYear(value);
  };

  const barData = Object.keys(dashboardData.monthlyAppointments).map((key) => ({
    clinic: key,
    appointments: dashboardData.monthlyAppointments[key]['7'],
  }));

  const barConfig = {
    data: barData,
    xField: 'clinic',
    yField: 'appointments',
    seriesField: 'clinic',
    colorField: 'clinic',
    legend: {
      position: 'top-left',
    },
    xAxis: {
      label: {
        autoRotate: true,
      },
    },
    barWidthRatio: 0.6,
    color: ['#1890ff'],
  };

  return (
    <>
      <ManagerSidebar />
      <Layout style={{ padding: '24px' }}>
        <Content style={{ padding: '24px', margin: 0, minHeight: '85vh', backgroundColor: '#fff' }}>
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Title level={2}>Manager Dashboard</Title>
            </Col>
            <Col span={6}>
              <Select
                defaultValue={year}
                style={{ width: '100%' }}
                onChange={handleYearChange}
              >
                {[2022, 2023, 2024, 2025].map((year) => (
                  <Option key={year} value={year}>
                    {year}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={6}>
              <Card bordered={false}>
                <Title level={4} style={{ margin: 0 }}>Daily Appointments</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={3} style={{ margin: 0 }}>{dashboardData.dailyAppointments}</Title>
              </Card>
            </Col>
            <Col span={6}>
              <Card bordered={false}>
                <Title level={4} style={{ margin: 0 }}>Total Appointments This Month</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={3} style={{ margin: 0 }}>{dashboardData.totalAppointmentsInMonthNow}</Title>
              </Card>
            </Col>
            <Col span={6}>
              <Card bordered={false}>
                <Title level={4} style={{ margin: 0 }}>Total Appointments This Year</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={3} style={{ margin: 0 }}>{dashboardData.totalAppointmentsInYearNow}</Title>
              </Card>
            </Col>
          </Row>
          <Divider style={{ margin: '24px 0' }} />
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Card>
                <Title level={4}>Monthly Appointments by Clinic</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Bar {...barConfig} />
              </Card>
            </Col>
          </Row>
        </Content>
      </Layout>
    </>
  );
};

export default ManagerDashboard;
