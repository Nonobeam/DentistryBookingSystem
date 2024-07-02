import React, { useEffect, useState } from 'react';
import { Button, Card, Table } from 'antd';
import { HiOutlineMail } from 'react-icons/hi';

import { useParams } from 'react-router-dom';
import { notification } from 'antd';
import { CustomerListDash } from '../../CustomerList';
import { EmailPopup } from '../EmailPopup';
import { CustomerServicess } from '../../../../../../services/CustomerServicess/CustomerServicess';

export default function CustomerInfo() {
  const { customerID } = useParams();
  const [info, setInfo] = useState([{}]);
  const [user, setUser] = useState({});
  const [appointmentData, setAppointmentData] = useState([]);
  const [apiData, setApiData] = useState([]);

  useEffect(() => {
    fetchData();
  }, [customerID]);

  const fetchData = async () => {
    try {
      const response = await CustomerServicess.getCustomerById(customerID);
      console.log(response);
        setUser(response.userDTO);
        setInfo(response.appointment);

      if (Array.isArray(response.appointment)) {
      //   const aData = response.appointment.map((item) => ({
      //     ...item,
      //     key: item.id,
      //     date: item.date,
      //     feedback: item.feedback,
      //     booking: item.user.name,
      //     services: item.services.name,
      //     clinic: item.clinic.name,
      //     dentist: item.dentist.user.name,
      //     dependent: item.dependent.name ? item.dependent.name : item.user.name,
      //   }));

      //   setAppointmentData(aData);
      }
    } catch (error) {
      console.log('Error fetching data:', error);
      notification.error({
        message: 'Error',
        description: error.message,
        onClick: () => {
          console.log('Notification Clicked!');
        },
      });
    }
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

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'TimeSlot',
      dataIndex: 'timeSlot',
      key: 'timeSlot',
    },
    {
      title: 'User',
      dataIndex: 'user',
      key: 'user',
    },
    {
      title: 'Dentist',
      dataIndex: 'dentist',
      key: 'dentist',
    },
    {
      title: 'Services',
      dataIndex: 'services',
      key: 'services',
    },
    // {
    //   title: 'Treatment',
    //   dataIndex: 'treatment',
    //   key: 'treatment',
    //   render: (text) => <span style={styles.treatment}>{text}</span>,
    // },
  ];

  return (
    <div
      className='customer-info'
      style={{
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        gap: '10px',
      }}>
      <h2>Customer Information</h2>
      <EmailPopup />
      <div>
        <strong>Name:</strong> {user.name}
      </div>
      <div>
        <strong>Email:</strong> {user.mail}
      </div>
      <div>
        <strong>Phone:</strong> {user.phone}
      </div>
      <div>
        <strong>Birthday:</strong> {user.birthday}
      </div>
      <div>
        <Card title='Appointment History' style={styles.card}>
          <Table
            dataSource={info}
            columns={columns}
            pagination={false}
            bordered
            size='small'
          />
        </Card>
      </div>
    </div>
  );
}
