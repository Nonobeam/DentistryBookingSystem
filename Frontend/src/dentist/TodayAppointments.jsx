import React, { useEffect, useState } from 'react';
import { Layout, Card, Col, Row, message, Empty, Button, Modal, Input, Typography, Spin, Statistic } from 'antd';
import { CalendarOutlined, UserOutlined, MedicineBoxOutlined, TeamOutlined, BellOutlined } from '@ant-design/icons';
import axios from 'axios';
import Sidebar from './Sidebar';

const { Header, Content } = Layout;
const { Title, Text } = Typography;

const TodayAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reminderModalVisible, setReminderModalVisible] = useState(false);
  const [reminderMessage, setReminderMessage] = useState("");
  const [clinicInfo, setClinicInfo] = useState("");

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
    fetchClinicInfo();
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
          style={{ 
            padding: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
          }}
        >
          <Title level={3} style={{ color: 'white', margin: 0 }}>
            {clinicInfo}
          </Title>
        </Header>
        <Content style={{ margin: '24px 16px 0', overflow: 'initial' }}>
          <Card>
            <Row gutter={[16, 16]} align="middle">
              <Col xs={24} sm={12}>
                <Title level={2}>
                  <CalendarOutlined /> Today's Appointments
                </Title>
              </Col>
              <Col xs={24} sm={12} style={{ textAlign: 'right' }}>
                <Statistic title="Total Appointments" value={appointments.length} />
            <Button
              type="primary"
                  icon={<BellOutlined />}
                  onClick={() => setReminderModalVisible(true)}
                  style={{ marginTop: '16px' }}
            >
              Send Reminder
            </Button>
              </Col>
            </Row>

            {loading ? (
              <div style={{ textAlign: 'center', margin: '50px 0' }}>
                <Spin size="large" />
              </div>
            ) : appointments.length === 0 ? (
              <Empty description="No appointments today" />
            ) : (
              <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
                {appointments.map((appointment) => (
                  <Col xs={24} sm={12} md={8} lg={6} key={appointment.appointmentId}>
                    <Card
                      hoverable
                      title={
                        <Text strong>
                          <CalendarOutlined /> {appointment.timeSlot}
                        </Text>
                      }
                    >
                      <p><UserOutlined /> Patient: {appointment.user}</p>
                      <p><MedicineBoxOutlined /> Service: {appointment.services}</p>
                      <p><TeamOutlined /> Staff: {appointment.staff}</p>
                    </Card>
                  </Col>
                ))}
              </Row>
            )}
          </Card>
        </Content>
      </Layout>

            <Modal
        title={<><BellOutlined /> Send Reminder</>}
        visible={reminderModalVisible}
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
    </Layout>
  );
};

export default TodayAppointments;