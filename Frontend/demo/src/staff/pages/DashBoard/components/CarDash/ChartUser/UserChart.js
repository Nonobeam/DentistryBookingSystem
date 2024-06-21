import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

const UserChart = ({ data }) => {
  const chartRef = useRef();

  useEffect(() => {
    const ctx = chartRef.current.getContext('2d');
    const chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.map((item) => item.month),
        datasets: [
          {
            label: 'Total Users',
            data: data.map((item) => item.users),
            backgroundColor: 'rgba(54, 162, 235, 0.5)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });

    return () => {
      chart.destroy();
    };
  }, [data]);

  return <canvas ref={chartRef} />;
};

export default UserChart;
