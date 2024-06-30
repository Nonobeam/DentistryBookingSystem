import React, { useEffect, useState } from 'react';
import { Card, Table } from 'antd';
import { AppointmentHistoryServices } from '../../../../services/AppointmentHistoryServices/AppointmentHistoryServices';


const AppointmentHistory = () => {
  const [apiData, setApiData] = useState([]);

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'User',
      dataIndex: 'user',
      key: 'user',
    },
    {
      title: 'TimeSlot',
      dataIndex: 'timeSlot',
      key: 'timeSlot',
    },
    {
      title: 'Dentist',
      dataIndex: 'dentist',
      key: 'dentist',
    },
    {
      title: 'Services', 
      dataIndex: 'services',
      key: 'services',},

    // {
    //   title: 'Treatment',
    //   dataIndex: 'treatment',
    //   key: 'treatment',
    //   render: (text) => (
    //     <span style={styles.treatment}>{text}</span>
    //   ),
    // },
  ];
  const styles = {
    card: {
      marginBottom: '20px',
    },
    treatment: {
      backgroundColor: '#f0f0f0',
      padding: '5px 10px',
      borderRadius: '5px',
    },
  }; useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await AppointmentHistoryServices.getAll();
        setApiData(response);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };
    fetchData();
  }, []);
  return (
    <div>
      <Card title="Appointment History" style={styles.card}>
        <Table
          dataSource={apiData}
          columns={columns}
          pagination={false}
          bordered
          size="small"
        />
      </Card>
    </div>
  );
};



export default AppointmentHistory;
