import React, { useEffect, useState } from 'react';
import { Button, Card, Table, notification } from 'antd';
import { HiOutlineMail } from 'react-icons/hi';
import { EmailPopup } from './components/EmailPopup';
import { useParams } from 'react-router-dom';
import { DentistServices } from '../../../../../../services/DentistServices/DentistServices';
import moment from 'moment';

export default function DentistInfo() {
  const { dentistID } = useParams();
  const [user, setUser] = useState({});
  const [appointments, setAppointments] = useState([]);

  useEffect(() => {
    fetchData();
  }, [dentistID]);

  const fetchData = async () => {
    try {
      const response = await DentistServices.getDentistById(dentistID);
      setUser(response.userDTO);

      if (Array.isArray(response.appointment)) {
        const appointmentData = response.appointment.map((item) => ({
          ...item,
          key: item.appointmentId,
          date: moment(item.date).format('YYYY-MM-DD'),
          timeSlot: item.timeSlot,
          dentist: item.dentist,
          services: item.services,
          user: item.dependent ? `Customer: ${item.user}, Dependent: ${item.dependent}` : `Customer: ${item.user}`,
        }));
        setAppointments(appointmentData);
      }
    } catch (error) {
      console.log('Error fetching data:', error);
      notification.error({
        message: 'Error',
        description: error.message,
      });
    }
  };

  const styles = {
    card: {
      marginBottom: '20px',
    },
    infoContainer: {
      display: 'flex',
      justifyContent: 'center',
      flexDirection: 'column',
      gap: '10px',
      marginBottom:'100px'
    },
  };

  const columns = [
    {
      title: 'Date',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: 'Time Slot',
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
  ];

  return (
    <div className='dentist-info' style={{ padding: '20px' }}>
      <h2 >Dentist Information</h2>
      <EmailPopup user={user} />
      <div style={styles.infoContainer}>
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
      </div>
      <Card title='Appointment History' style={styles.card}>
        <Table dataSource={appointments} columns={columns} pagination={false} bordered size='small' />
      </Card>
    </div>
  );
}
