import React, { useState, useEffect } from 'react';
import { Layout, Select, Card, Row, Col, Typography, Divider, Empty } from 'antd';
import axios from 'axios';
import { Bar } from '@ant-design/charts';
import ManagerSidebar from './ManagerSidebar';

const { Header, Content } = Layout;
const { Option } = Select;
const { Title } = Typography;

const ManagerDashboard = () => {
  const [year, setYear] = useState(2024);
  const [dashboardData, setDashboardData] = useState({
    dailyAppointments: 0,
    monthlyAppointments: {},
    totalAppointmentsInMonthNow: 0,
    totalAppointmentsInYearNow: 0,
    ratingDentist: {} // Initialize ratingDentist state
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchDashboardData(year);
    fetchRatingDentist();
  }, [year]); // Fetch data when year changes

  const fetchDashboardData = async (selectedYear) => {
    setLoading(true);
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/v1/manager/dashboard?year=${selectedYear}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = response.data;
      // Ensure dailyAppointments is not null or undefined
      const dailyAppointments = data.dailyAppointments || 0;
      const totalAppointmentsInMonthNow = data.totalAppointmentsInMonthNow || 0;
      const totalAppointmentsInYearNow = data.totalAppointmentsInYearNow || 0;
      setDashboardData({
        dailyAppointments,
        monthlyAppointments: data.monthlyAppointments || {},
        totalAppointmentsInMonthNow,
        totalAppointmentsInYearNow,
        ratingDentist: dashboardData.ratingDentist // Preserve existing ratingDentist
      });
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchRatingDentist = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/v1/manager/ratingDentist', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const ratingDentistData = response.data || {};
      setDashboardData(prevState => ({
        ...prevState,
        ratingDentist: ratingDentistData
      }));
    } catch (error) {
      console.error('Error fetching rating dentist data:', error);
    }
  };

  const handleYearChange = (value) => {
    setYear(value);
  };

  const barData = Object.keys(dashboardData.monthlyAppointments).map((key) => ({
    clinic: key,
    appointments: dashboardData.monthlyAppointments[key]['7'] || 0,
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
    <Layout style={{ minHeight: '100vh' }}>
      <ManagerSidebar />

      <Layout style={{ padding: '24px' }}>
        <Content style={{ padding: '24px', margin: 0, minHeight: '85vh', backgroundColor: '#fff' }}>
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Title level={2}>Manager Dashboard</Title>
            </Col>
            <Col span={7}>
              <Card
                bordered={false}
                style={{ backgroundColor: '#f0f9ff', textAlign: 'center', padding: '16px 0' }}
              >
                <Title level={5}>Appointments Today</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={4}>{dashboardData.dailyAppointments}</Title>
              </Card>
            </Col>
            <Col span={7}>
              <Card
                bordered={false}
                style={{ backgroundColor: '#fcffe6', textAlign: 'center', padding: '16px 0' }}
              >
                <Title level={5}>This Month</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={4}>{dashboardData.totalAppointmentsInMonthNow}</Title>
              </Card>
            </Col>
            <Col span={7}>
              <Card
                bordered={false}
                style={{ backgroundColor: '#ffffb8', textAlign: 'center', padding: '16px 0' }}
              >
                <Title level={5}>This Year</Title>
                <Divider style={{ margin: '12px 0' }} />
                <Title level={4}>{dashboardData.totalAppointmentsInYearNow}</Title>
              </Card>
            </Col>
            <Col span={3}>
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
          </Row>
          <Divider style={{ margin: '24px 0' }} />
          <Row gutter={[16, 16]}>
            <Col span={12}>
              <Card loading={loading}>
                <Title level={4}>Monthly Appointments by Clinic</Title>
                <Divider style={{ margin: '12px 0' }} />
                {(
                  barData.length > 0 ? (
                    <Bar {...barConfig} />
                  ) : (
                    <Empty description="No data available" />
                  )
                )}
              </Card>
            </Col>
            <Col span={12}>
              <Card>
                <Title level={4}>Rating of Dentists</Title>
                <Divider style={{ margin: '12px 0' }} />
                {Object.keys(dashboardData.ratingDentist).length > 0 ? (
                  <ul>
                    {Object.entries(dashboardData.ratingDentist).map(([dentist, rating]) => (
                      <li key={dentist}>
                        <Typography.Text>{`${dentist}: ${rating}`}</Typography.Text>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <Empty description="No rating data available" />
                )}
              </Card>
            </Col>
          </Row>
        </Content>
      </Layout>
    </Layout>
  );
};

export default ManagerDashboard;
