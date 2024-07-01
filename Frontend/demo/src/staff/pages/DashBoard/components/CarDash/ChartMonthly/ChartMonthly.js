import { Card } from 'antd';
import React from 'react';
import { Bar } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export const ChartMonthly = ({ monthlyAppointmentsData }) => {
  return (
    <Card style={{ width: '100%', height: '400px', margin: '0 auto'}} title='Monthly Appointments'>
      <Bar data={monthlyAppointmentsData} />
    </Card>
  );
};
