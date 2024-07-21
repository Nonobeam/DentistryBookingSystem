import React, { useEffect, useState } from 'react';
import { Card, Table, notification, Spin } from 'antd';
import { EmailPopup } from './components/EmailPopup';
import { useParams } from 'react-router-dom';
import { DentistServices } from '../../../../../../services/DentistServices/DentistServices';
import dayjs from 'dayjs';
import { StarFilled } from '@ant-design/icons';

export default function DentistInfo() {
  const { dentistID } = useParams();
  const [user, setUser] = useState({});
  const [rate, setRate] = useState();
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(false); // Thêm state loading

  useEffect(() => {
    fetchData();
  }, [dentistID]);

  const fetchData = async () => {
    setLoading(true); // Bật loading khi bắt đầu fetch dữ liệu
    try {
      const response = await DentistServices.getDentistById(dentistID);
      setUser(response.userDTO);

      if (Array.isArray(response.appointment)) {
        const appointmentData = response.appointment.map((item) => ({
          ...item,
          key: item.appointmentId,
          date: dayjs(item.date).format('YYYY-MM-DD'),
          timeSlot: item.timeSlot,
          dentist: item.dentist,
          services: item.services,
          user: item.dependent ? `Customer: ${item.user}, Dependent: ${item.dependent}` : `Customer: ${item.user}`,
        }));
        setAppointments(appointmentData);
        setRate(response.star);
      }
    } catch (error) {
      console.log('Error fetching data:', error);
      notification.error({
        message: 'Error',
        description: error.message,
      });
    } finally {
      setLoading(false); // Tắt loading khi fetch dữ liệu hoàn thành
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
      marginBottom: '100px'
    },
    starContainer: {
      display: 'flex',
      alignItems: 'center',
    },
    starIcon: {
      fontSize: '20px',
      color: '#FFD700',
      marginRight: '5px',
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

  const renderRateStars = () => {
    const stars = [];
    for (let i = 1; i <= 5; i++) {
      if (i <= rate) {
        stars.push(<StarFilled key={i} style={styles.starIcon} />);
      } else {
        stars.push(<StarFilled key={i} style={{ ...styles.starIcon, color: '#D3D3D3' }} />);
      }
    }
    return stars;
  };

  return (
    <div className='dentist-info' style={{ padding: '20px' }}>
      <h2>Dentist Information</h2>
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
        <div style={styles.starContainer}>
          <strong>Rate : </strong> {renderRateStars()}
        </div>
      </div>
      <Card title='Appointment History' style={styles.card}>
        <Spin spinning={loading}>
          <Table dataSource={appointments} columns={columns} pagination={false} bordered size='small' />
        </Spin>
      </Card>
    </div>
  );
}
