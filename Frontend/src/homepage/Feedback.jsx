import React, { useEffect, useState } from "react";
import { Layout, Typography, Table, Button, Modal, Form, Input, message, Spin, Rate, Card, Empty } from "antd";
import { MessageOutlined, StarOutlined } from '@ant-design/icons';
import "antd/dist/reset.css";
import axios from "axios";
import dayjs from "dayjs";
import NavBar from "./Nav";

const { Content } = Layout;
const { Title, Text } = Typography;

const FeedbackPage = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modalVisible, setModalVisible] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const [feedback, setFeedback] = useState("");
  const [starRating, setStarRating] = useState(0);

  const fetchAppointments = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await axios.get("http://localhost:8080/user/appointment-feedback", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAppointments(response.data);
    } catch (error) {
      message.error("Failed to fetch appointments");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAppointments();
  }, []);

  const handleUpdateFeedback = async () => {
    const token = localStorage.getItem("token");
    
    if (starRating === 0) {
      message.error("Please rate the appointment.");
      return;
    }
  
    try {
      await axios.put(`http://localhost:8080/user/appointment-feedback/${selectedAppointmentId}`, { 
        feedback,
        starAppointment: starRating 
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Feedback updated successfully");
      setModalVisible(false);
      setFeedback("");
      setStarRating(0);
      setSelectedAppointmentId(null);
      fetchAppointments();
    } catch (error) {
      message.error("Failed to update feedback");
    }
  };
  
  const columns = [
    {
      title: "Patient",
      dataIndex: "user",
      key: "user",
      render: (_, record) => (record.dependent ? record.dependent.name : record.user.name),
    },
    {
      title: "Date & Time",
      dataIndex: "date",
      key: "date",
      render: (date, record) => (
        <span>
          {dayjs(date).format("DD-MM-YYYY")} at {record.timeSlot.startTime}
        </span>
      ),
      sorter: (a, b) => dayjs(a.date).unix() - dayjs(b.date).unix(),
    },
    {
      title: "Service",
      dataIndex: "services",
      key: "services",
      render: (services) => services.name,
    },
    {
      title: "Clinic",
      dataIndex: "clinic",
      key: "clinic",
      render: (clinic) => clinic.name,
    },
    {
      title: "Dentist",
      dataIndex: "dentist",
      key: "dentist",
      render: (dentist) => dentist.user.name,
    },
    {
      title: "Actions",
      key: "actions",
      render: (_, record) => (
        <Button 
          type="primary" 
          icon={<MessageOutlined />}
          onClick={() => {
            setSelectedAppointmentId(record.appointmentID);
            setModalVisible(true);
          }}
        >
          Give Feedback
        </Button>
      ),
    },
  ];

  const locale = {
    emptyText: 'There are no appointment need to feedback.',
  };

  return (
    <Layout style={{ minHeight: '100vh', backgroundColor: '#f0f2f5' }}>
      <NavBar />
      <Content style={{ padding: '20px', paddingTop: "100px", maxWidth: 1200, margin: '0 auto' }}>
        <Card>
          <Title level={2} style={{ marginBottom: 24 }}>
            <StarOutlined style={{ marginRight: 8, color: '#1890ff' }} />
            Appointment Feedback
          </Title>
          <Text type="secondary" style={{ marginBottom: 24, display: 'block' }}>
            Please provide feedback for your recent appointments. Your input helps us improve our services.
          </Text>

            <Table 
              loading={loading}
              locale={locale}
              columns={columns} 
              dataSource={appointments} 
              rowKey="appointmentID" 
              pagination={{ pageSize: 10 }}
              style={{ backgroundColor: 'white' }}
            />

        </Card>
        <Modal
          title={
            <span>
              <StarOutlined style={{ color: '#1890ff', marginRight: 8 }} />
              Provide Feedback
            </span>
          }
          open={modalVisible}
          onCancel={() => {
            setModalVisible(false);
            setFeedback("");
            setStarRating(0);
          }}
          onOk={handleUpdateFeedback}
          okText="Submit Feedback"
        >
          <Form layout="vertical">
            <Form.Item 
              label="How would you rate your experience?"
              required
              tooltip="This field is required"
            >
              <Rate 
                value={starRating} 
                onChange={value => setStarRating(value)} 
                style={{ fontSize: 36 }}
              />
            </Form.Item>
            <Form.Item label="Additional comments (optional)">
              <Input.TextArea
                value={feedback}
                onChange={(e) => setFeedback(e.target.value)}
                placeholder="Tell us more about your experience..."
                rows={4}
              />
            </Form.Item>
          </Form>
        </Modal>
      </Content>
    </Layout>
  );
};

export default FeedbackPage;
