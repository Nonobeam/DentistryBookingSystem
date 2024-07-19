import React, { useEffect, useState } from 'react';
import { Layout, Card, Col, Row, message, Empty, Button, Modal, Input } from 'antd';
import axios from 'axios';
import Sidebar from './Sidebar';

const { Header, Content } = Layout;

const TodayAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reminderModalVisible, setReminderModalVisible] = useState(false);
  const [reminderMessage, setReminderMessage] = useState("");
  const [clinicInfo, setClinicInfo] = useState(""); // Add state for clinic info

  const fetchClinicInfo = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get('http://localhost:8080/api/v1/dentist/clinic', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setClinicInfo(response.data);
    } catch (error) {
      message.error(error.response?.data || "An error occurred while fetching clinic information");
      console.error(error);
    }
  };
  const handleSendReminder = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      await axios.post(
        `http://localhost:8080/api/v1/dentist/reminder`,
        {
          message: reminderMessage
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      message.success("Reminder sent successfully");
      setReminderModalVisible(false);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchClinicInfo(); // Fetch clinic info on component mount
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
      <Header
          className="site-layout-sub-header-background"
          style={{ 
            padding: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            height: '64px' // Default Ant Design Header height
          }}
        >
          <div style={{ 
            color: 'white', 
            fontFamily: 'Georgia', 
            fontSize: '22px', 
            textAlign: 'center' 
          }}>
            {clinicInfo}
          </div>
        </Header>        <Content style={{ margin: '24px 16px 0', overflow: 'initial' }}>
          <div className="site-layout-background" style={{ padding: 24, minHeight: 360 }}>
            <h1>DENTIST</h1>
            <h2>Today's Appointment</h2>
            <Button
              type="primary"
              onClick={() => {
                setReminderModalVisible(true);
              }}
            >
              Send Reminder
            </Button>
            <Modal
              title="Send Reminder"
              open={reminderModalVisible}
              onOk={handleSendReminder}
              onCancel={() => setReminderModalVisible(false)}
              confirmLoading={loading}
            >
              <Input.TextArea
                rows={4}
                value={reminderMessage}
                onChange={(e) => setReminderMessage(e.target.value)}
                placeholder="Type your reminder message here..."
              />
            </Modal>
            {loading ? (
              <p>Loading...</p>
            ) : appointments.length === 0 ? (
              <Empty description="No appointments today" />
            ) : (
              <Row gutter={16}>
                {appointments.map((appointment) => (
                  <Col span={6} key={appointment.appointmentId}>
                    <Card title={`${appointment.timeSlot}`}>
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
