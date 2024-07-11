import { Card } from 'antd';
import React from 'react';
import { Pie } from 'react-chartjs-2';
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export const ChartDailyAppointment = ({ dailyAppointmentsData }) => {
  const pieChartOptions = {
    responsive: true,
    maintainAspectRatio: false, // This ensures the chart can be smaller than the container
    plugins: {
      legend: {
        position: 'top', // Adjust the legend position as needed
      },
    },
  };
  return (
    <Card style={{}} title='Daily Appointments'>
      <Pie
        style={{ width: '600px', height: '400px', margin: '0 auto', textAlign: 'center' }}
        data={dailyAppointmentsData}
        options={pieChartOptions}
      />
    </Card>
  );
};
