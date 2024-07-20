import React, { useEffect, useState } from "react";
import { Layout, Typography, Table, Button, Modal, Form, Input, message, Spin, Rate } from "antd";
import "antd/dist/reset.css";
import axios from "axios";
import dayjs from "dayjs";
import NavBar from "./Nav";

const { Content } = Layout;

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
    
    // Kiểm tra xem người dùng đã nhập số sao hay chưa
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
      title: "Patient Name",
      dataIndex: "user",
      key: "user",
      render: (_, record) => (record.dependent ? record.dependent.name : record.user.name),
    },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
      render: (date) => dayjs(date).format("DD-MM-YYYY"),
      sorter: (a, b) => dayjs(a.date).unix() - dayjs(b.date).unix(),
    },
    {
      title: "Time",
      dataIndex: "timeSlot",
      key: "timeSlot",
      render: (timeSlot) => timeSlot.startTime,
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
        <Button type="primary" onClick={() => {
            setSelectedAppointmentId(record.appointmentID);
            setModalVisible(true);
        }}>
          Update Feedback
        </Button>
      ),
    },
  ];

  return (
    <Layout>
      <NavBar />
      <Content style={{ padding: '20px', paddingTop: "100px"}}>
        <Typography.Title level={2}>Appointment Feedback</Typography.Title>
        {loading ? (
          <Spin size="large" />
        ) : (
          <Table columns={columns} dataSource={appointments} rowKey="id" />
        )}
        <Modal
          title="Update Feedback"
          visible={modalVisible}
          onCancel={() => setModalVisible(false)}
          onOk={handleUpdateFeedback}
        >
          <Form layout="vertical">
            <Form.Item label="Rating">
              <Rate value={starRating} onChange={value => setStarRating(value)} />
            </Form.Item>
            <Form.Item label="Feedback">
              <Input.TextArea
                value={feedback}
                onChange={(e) => setFeedback(e.target.value)}
              />
            </Form.Item>
          </Form>
        </Modal>
      </Content>
    </Layout>
  );
};

export default FeedbackPage;
