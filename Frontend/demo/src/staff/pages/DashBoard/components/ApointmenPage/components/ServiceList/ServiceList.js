// ServiceList.js
import React from 'react';
import { Card } from 'antd';

export const ServiceList = ({ services }) => {
  return (
    <div style={{ maxWidth: 400, margin: '0 auto', padding: 20 }}>
      {services.map((service) => (
        <Card key={service.serviceID} style={{ marginBottom: 10 }}>
          <p style={{ fontWeight: 'bold' }}>{service.name}</p>
        </Card>
      ))}
    </div>
  );
};