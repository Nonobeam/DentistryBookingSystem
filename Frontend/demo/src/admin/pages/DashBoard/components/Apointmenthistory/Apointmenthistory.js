import React from 'react';
import { Card, Table } from 'antd';

const AppointmentHistory = ({ appointments }) => {
  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'Time',
      dataIndex: 'time',
      key: 'time',
    },
    {
      title: 'Patient Name',
      dataIndex: 'patientName',
      key: 'patientName',
    },
    {
      title: 'Dentist',
      dataIndex: 'dentist',
      key: 'dentist',
    },
    {
      title: 'Treatment',
      dataIndex: 'treatment',
      key: 'treatment',
      render: (text) => (
        <span style={styles.treatment}>{text}</span>
      ),
    },
  ];

  return (
    <div>
      <Card title="Appointment History" style={styles.card}>
        <Table
          dataSource={appointments}
          columns={columns}
          pagination={false}
          bordered
          size="small"
        />
      </Card>
    </div>
  );
};

const styles = {
  card: {
    marginBottom: '20px',
  },
  treatment: {
    backgroundColor: '#f0f0f0',
    padding: '5px 10px',
    borderRadius: '5px',
  },
};

export default AppointmentHistory;
