import React, { useEffect, useState } from 'react';
import { Layout, Card, Col, Row, message, Empty } from 'antd';
import axios from 'axios';
import Sidebar from './Sidebar';

const { Header, Content } = Layout;

const TodayAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAppointments = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get('http://localhost:8080/api/v1/dentist/appointment-today', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        
        if (Array.isArray(response.data)) {
          setAppointments(response.data);
        } else {
          setAppointments([]);
          message.info(response.data);
        }
      } catch (error) {
        message.error('Failed to fetch appointments');
      } finally {
        setLoading(false);
      }
    };

    fetchAppointments();
  }, []);

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sidebar />
      <Layout>
        <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />
        <Content style={{ margin: '24px 16px 0', overflow: 'initial' }}>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
            <h1>DENTIST</h1>
            <h2>Today's Appointment</h2>
            {loading ? (
              <p>Loading...</p>
            ) : appointments.length === 0 ? (
              <Empty description="No appointments today" />
            ) : (
              <Row gutter={16}>
                {appointments.map((appointment) => (
                  <Col span={6} key={appointment.appointmentId}>
                    <Card title={`${appointment.timeSlot}`} 
                    >
                      <p>Patient: {appointment.user}</p>
                      <p>Service: {appointment.services}</p>
                      <p>Staff: {appointment.staff}</p>
                    </Card>
                  </Col>
                ))}
              </Row>
            )}
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default TodayAppointments;
