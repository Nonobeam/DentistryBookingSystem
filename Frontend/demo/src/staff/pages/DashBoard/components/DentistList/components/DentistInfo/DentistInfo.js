import React, { useEffect, useState } from 'react';
import { Button, Card, Table } from 'antd';
import { HiOutlineMail } from 'react-icons/hi';
import { EmailPopup } from './components/EmailPopup';
import { useParams } from 'react-router-dom';
import { DentistServices } from '../../../../../../services/DentistServices/DentistServices';
import { dataDentistDetail } from '../../../../../../utils/data';

export default function DentistInfo() {
  const id = useParams().dentistID;
  const [info, setInfo] = useState(dataDentistDetail);
  const [user, setUser] = useState({});
  const [apointmentData, setAppointmentData] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await DentistServices.getDentistById(id);
        console.log(response);
        // setInfo(response);
      } catch (error) {
        console.log(error);
      }
    };
    fetchData();
    const { userDTO, appointment } = info;
    setUser(userDTO);
    const aData = appointment.map((item) => ({
      ...item,
      key: item.id,
      date: item.date,
      feedback: item.feedback,
      booking: item.user.name,
      services: item.services.name,
      clinic: item.clinic.name,
      dentist: item.dentist.user.name,
      dependent: item.dependent.name ? item.dependent.name : item.user.name,
    }));
    setAppointmentData(aData);
  }, []);

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
      title: 'Feedback',
      dataIndex: 'feedback',
      key: 'feedback',
    },
    {
      title: 'Booking',
      dataIndex: 'booking',
      key: 'booking',
    },
    {
      title: 'Patient',
      dataIndex: 'dependent',
      key: 'dependent',
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
    {
      title: 'Clinic',
      dataIndex: 'clinic',
      key: 'clinic',
    },
    {
      title: 'Treatment',
      dataIndex: 'treatment',
      key: 'treatment',
      render: (text) => <span style={styles.treatment}>{text}</span>,
    },
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
      <h2>Dentist Information</h2>
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
            dataSource={apointmentData}
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
