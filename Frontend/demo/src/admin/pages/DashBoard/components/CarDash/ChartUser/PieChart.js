import React from 'react';
import { Pie } from 'react-chartjs-2';

const AppointmentPieChart = ({ totalUserData }) => {
  // Convert totalUserData into the format required by the Pie chart
  const appointmentData = {
    labels: totalUserData.map(data => data.month),
    datasets: [
      {
        label: 'Appointments',
        data: totalUserData.map(data => data.users),
        backgroundColor: ['#ff6384', '#36a2eb', '#ffce56', '#ff6384', '#36a2eb', '#ffce56', '#ff6384', '#36a2eb', '#ffce56', '#ff6384', '#36a2eb', '#ffce56'], // Adding more colors for additional data points
        borderWidth: 1,
      },
    ],
  };

  // Options for the Pie Chart
  const options = {
    title: {
      display: true,
      text: 'Appointment Distribution Throughout the Year',
      fontSize: 16,
      fontColor: '#333',
      fontFamily: 'Arial',
    },
    legend: {
      display: true,
      position: 'bottom',
      labels: {
        fontColor: '#333',
      },
    },
    responsive: true,
    maintainAspectRatio: false,
  };

  return <Pie data={appointmentData} options={options} />;
};

export default AppointmentPieChart;
