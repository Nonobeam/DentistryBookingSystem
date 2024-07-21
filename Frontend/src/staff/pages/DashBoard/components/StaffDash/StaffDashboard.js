import React, { useEffect, useState } from 'react';
import { Card, Col, Row, Button, DatePicker, Input, Statistic } from 'antd';
import { ChartDailyAppointment } from './ChartDailyAppointment/ChartDailyAppointment';
import { ChartMonthly } from './ChartMonthly/ChartMonthly';
import { ChartRating } from './ChartRatingDentist/ChartRating';
import { DashBoardServices } from '../../../../services/DashBoardServices/DashBoardServices';

export const StaffDashboard = () => {
  const [dashboardData, setDashboardData] = useState({});
  const [inputDate, setInputDate] = useState('');
  const [inputYear, setInputYear] = useState('');

  useEffect(() => {
    fetchData(inputDate, inputYear);
  }, []);

  const fetchData = async (date, year) => {
    try {
      const response = await DashBoardServices.getAll({ date, year });
      setDashboardData(response || {});
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const handleDateChange = ( dateString) => {
    setInputDate(dateString);
  };

  const handleSearch = () => {
    fetchData(inputDate, inputYear);
  };

  const dailyAppointmentsData = {
    labels: Object.keys(dashboardData.dailyAppointments || {}),
    datasets: [{
      label: 'Daily Appointments',
      data: Object.values(dashboardData.dailyAppointments || {}),
      backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
    }],
  };

  const monthlyAppointmentsData = {
    labels: Object.keys(dashboardData.monthlyAppointments || {}).map(month => `Month ${month}`),
    datasets: [{
      label: 'Monthly Appointments',
      data: Object.values(dashboardData.monthlyAppointments || {}),
      backgroundColor: '#8884d8',
    }],
  };

  const ratingDentistData = dashboardData.ratingDentist || {};

  return (
    <div style={{ padding: '20px' }}>
      <Row gutter={[16, 16]}>
        <Col span={24}>
          <Card>
            <Row gutter={16}>
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
                <Button type='primary' onClick={handleSearch} style={{ width: '100%' }}>
                  Search
                </Button>
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={24}>
          <Card title="Appointments Overview">
            <Row gutter={[16, 16]}>
              <Col span={12}>
                <Statistic 
                  title="Total Appointments This Month" 
                  value={dashboardData.totalAppointmentsInMonthNow || 0} 
                />
              </Col>
              <Col span={12}>
                <Statistic 
                  title="Total Appointments This Year" 
                  value={dashboardData.totalAppointmentsInYearNow || 0} 
                />
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={12}>
          <Row gutter={[0, 16]}>
            <Col span={24}>
              <Card title="Monthly Appointments">
                <ChartMonthly monthlyAppointmentsData={monthlyAppointmentsData} />
              </Card>
            </Col>
            <Col span={24}>
              <Card title="Dentist Ratings">
                <ChartRating ratingDentistData={ratingDentistData} />
              </Card>
            </Col>
          </Row>
        </Col>
        <Col span={12}>
          <Card title="Daily Appointments" style={{ height: '100%' }}>
            <ChartDailyAppointment dailyAppointmentsData={dailyAppointmentsData} />
          </Card>
        </Col>
      </Row>
    </div>
  );
};