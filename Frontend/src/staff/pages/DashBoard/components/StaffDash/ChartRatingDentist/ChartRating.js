import React from 'react';
import { Bar } from 'react-chartjs-2';
import 'chart.js/auto';

export const ChartRating = ({ ratingDentistData }) => {
  const data = {
    labels: Object.keys(ratingDentistData || {}),
    datasets: [
      {
        label: 'Dentist Ratings',
        data: Object.values(ratingDentistData || {}),
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
      },
    ],
  };

  return (
    <Bar
      data={data}
      options={{
        responsive: true,
        scales: {
          x: {
            beginAtZero: true,
          },
          y: {
            beginAtZero: true,
          },
        },
      }}
    />
  );
};
