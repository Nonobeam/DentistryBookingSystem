import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Button, DatePicker, Input } from 'antd';
import { ChartDailyAppointment } from './ChartDailyAppointment/ChartDailyAppointment';
import { ChartMonthly } from './ChartMonthly/ChartMonthly';
import { DashBoardServices } from '../../../../services/DashBoardServices/DashBoardServices';
import moment from 'moment'; // Import moment for date formatting

export const StaffDashboard = () => {
  const [dashboardData, setDashboardData] = useState({});
  const [inputDate, setInputDate] = useState('');
  const [inputYear, setInputYear] = useState('');

  useEffect(() => {
    fetchData(inputDate, inputYear); // Fetch data initially with default inputDate and inputYear
  }, []);

  const fetchData = async (date, year) => {
    try {
      const response = await DashBoardServices.getAll({ date, year });
      console.log(response);
      setDashboardData(response || {}); // Set dashboard data from API response
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const handleDateChange = (date, dateString) => {
    setInputDate(dateString); // Set inputDate when DatePicker value changes
  };

  const handleSearch = () => {
    fetchData(inputDate, inputYear); // Call fetchData with current inputDate and inputYear
  };

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
    <>
      <Row gutter={16} style={{ marginBottom: '16px' }}>
        <Col span={8}>
          <DatePicker
            placeholder='Select Date'
            format='YYYY-MM-DD'
            onChange={handleDateChange}
            style={{ width: '100%' }}
          />
        </Col>
        <Col span={8}>
          <Input
            placeholder='Enter Year'
            value={inputYear}
            onChange={(e) => setInputYear(e.target.value)}
          />
        </Col>
        <Col span={8}>
          <Button
            type='primary'
            onClick={handleSearch}
            style={{ marginBottom: '16px', marginLeft: '8px' }}>
            Search
          </Button>
        </Col>
      </Row>
      <Row gutter={16}>
        <Col span={16}>
          <ChartDailyAppointment
            dailyAppointmentsData={dailyAppointmentsData}
          />
        </Col>
        <Col span={8}>
          <ChartMonthly monthlyAppointmentsData={monthlyAppointmentsData} />
        </Col>
        <Col span={8}>
          <Card
            title='Total Appointments Now'
            bordered={false}
            style={{ marginBottom: '16px' }}>
            {dashboardData.totalAppointmentsInMonthNow || 0}
          </Card>
        </Col>
        <Col span={8}>
          <Card
            title='Total Appointments In Year Now'
            bordered={false}
            style={{ marginBottom: '16px' }}>
            {dashboardData.totalAppointmentsInYearNow || 0}
          </Card>
        </Col>
      </Row>
    </>
  );
};
