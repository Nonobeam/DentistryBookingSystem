// CardTask.jsx
import React from 'react';
import { Card } from 'antd';

const { Meta } = Card;

export const CardTask = ({ data }) => {
  return (
    <div>
      {data.map((task, index) => (
        <Card key={index} style={{ marginBottom: '10px', padding: '10px' }}>
          <Meta
            title={`Dentist: ${task.dentistName}`}
            description={
              <div>
                <p>Customer: {task.customerName || 'N/A'}</p>
                <p>Service: {task.serviceName || 'N/A'}</p>
                <p>Status: {task.status === 'arranged' ? 'arranged' : 'On appointment'}</p>
              </div>
            }
          />
        </Card>
      ))}
    </div>
  );
};


