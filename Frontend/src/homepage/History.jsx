import React, { useEffect, useState, useCallback } from "react";
import "antd/dist/reset.css";
import axios from "axios";
import { Table, Button, Modal, Form, DatePicker, Select, message, Input, Rate } from "antd";
import { useNavigate } from "react-router-dom";
import { DeleteOutlined, CommentOutlined, ExclamationCircleOutlined, StarOutlined } from '@ant-design/icons';
import dayjs from "dayjs";
import NavBar from "./Nav";
import './Histyle.css';
import { Tag } from 'antd';

const { Option } = Select;


const History = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [feedbackLoading, setFeedbackLoading] = useState(false);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [filterDate, setFilterDate] = useState(null);
  const [filterStatus, setFilterStatus] = useState(null);
  const [cancelModalVisible, setCancelModalVisible] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const sortOrder = "descend";
  const [feedbackModalVisible, setFeedbackModalVisible] = useState(false);
  const [feedback, setFeedback] = useState("");
  const [starRating, setStarRating] = useState(0);
  const history = useNavigate();



  const fetchAppointments = useCallback(async () => {
    setLoading(true);
    try {
      let url = `http://localhost:8080/user/appointment-history`;

      if (filterDate || filterStatus !== null) {
        url += `?${filterDate ? `workDate=${filterDate.format("YYYY-MM-DD")}` : ""}${filterDate && filterStatus !== null ? "&" : ""
          }${filterStatus !== null && filterStatus !== undefined ? `status=${filterStatus}` : ""}`;
      }
      const token = localStorage.getItem("token");
      const response = await axios.get(url, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAppointments(response.data);
    } catch (error) {
      message.error("Failed to fetch appointment history");
    }
    setLoading(false);
  }, [filterDate, filterStatus]);

  useEffect(() => {
    fetchAppointments();
  }, [fetchAppointments, filterDate, filterStatus, sortOrder]);

  const handleCancel = async () => {
    try {
      setCancelLoading(true);
      const token = localStorage.getItem("token");

      await axios.put(
        `http://localhost:8080/user/delete-booking/${selectedAppointmentId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      message.success("Appointment cancelled successfully");
      setCancelLoading(false);
      fetchAppointments();
      setCancelModalVisible(false);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const handleUpdateFeedback = async () => {
    const token = localStorage.getItem("token");

    if (starRating === 0) {
      message.error("Please rate the appointment.");
      setFeedbackLoading(false);
      return;
    }

    try {
      setFeedbackLoading(true);
      await axios.put(`http://localhost:8080/user/appointment-feedback/${selectedAppointmentId}`, {
        feedback,
        starAppointment: starRating
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      message.success("Feedback updated successfully");
      setFeedbackLoading(false);
      setFeedbackModalVisible(false);
      setFeedback("");
      setStarRating(0);
      setSelectedAppointmentId(null);
      fetchAppointments();
    } catch (error) {
      message.error("Failed to update feedback");
    }
  };

  const handleFeedbackButtonClick = (record) => {
    setSelectedAppointmentId(record.appointmentID);
    setFeedback(record.feedback || ""); // set existing feedback if available
    setStarRating(record.starAppointment || 0); // set existing star rating if available
    setFeedbackModalVisible(true);
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
      defaultSortOrder: sortOrder,
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
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        let color = 'default';
        let text = '';
        if (status === 0) {
          color = 'red';
          text = 'Cancelled';
        } else if (status === 1) {
          color = 'orange';
          text = 'Upcoming';
        } else if (status === 2) {
          color = 'green';
          text = 'Finished';
        }
        return <Tag color={color}>{text}</Tag>;
      },
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => {
        const appointmentDate = dayjs(record.date);
        const currentDate = dayjs();
        if (record.status === 1 && appointmentDate.isAfter(currentDate, "day")) {
          return (
            <>
              <Button
                type="primary"
                danger
                icon={<DeleteOutlined />}
                onClick={() => {
                  setSelectedAppointmentId(record.appointmentID);
                  setCancelModalVisible(true);
                }}
                style={{ marginRight: 8 }}
              >
                Cancel
              </Button>

            </>
          );
        } else if (record.status === 2) {
          if (record.starAppointment === 0) {
            return (
              <Button
                type="primary"
                icon={<CommentOutlined />}
                onClick={() => history("/appointment-feedback")}
                style={{ marginRight: 8, backgroundColor: "chocolate", borderColor: "chocolate" }}
              >
                Give Feedback
              </Button>
            );
          } else {
            return (
              <Button
                type="primary"
                icon={<CommentOutlined />}
                onClick={() => handleFeedbackButtonClick(record)}
                style={{ marginRight: 8, backgroundColor: "#52c41a", borderColor: "#52c41a" }}
              >
                Update Feedback
              </Button>
            );
          }
        }
        return null;
      },
    },

  ];

  const locale = {
    emptyText: 'There are no appointments.',
  };

  return (
    <div style={{
      backgroundColor: "#f0f2f5",
      minHeight: "100vh",
      
    }}>
      <NavBar />
      <div style={{ maxWidth: '90%', margin: "0 auto", padding: "20px", paddingTop: "100px" }}>
        <h1 style={{
          textAlign: "center",
          marginBottom: "30px",
          fontSize: "2.5rem",
          fontWeight: "bold"
        }}>Appointment History</h1>
        <div style={{
          background: "white",
          padding: "20px",
          borderRadius: "8px",
          boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
          marginBottom: "20px"
        }}>
          <Form layout="inline" style={{ justifyContent: "center", marginBottom: "20px" }}>
            <Form.Item label="Filter by Date">
              <DatePicker onChange={(date) => setFilterDate(date)} />
            </Form.Item>
            <Form.Item label="Filter by Status">
              <Select style={{ width: 150 }} onChange={(value) => setFilterStatus(value)} allowClear>
                <Option value={0}>Cancelled</Option>
                <Option value={1}>Upcoming</Option>
                <Option value={2}>Finished</Option>

              </Select>
            </Form.Item>
            <Form.Item>
              <Button type="primary" style={{ marginLeft: "10px" }} onClick={() => history("/booking")}>
                New Appointment
              </Button>
            </Form.Item>
          </Form>
        </div>

        <div style={{ background: "#fff", padding: "20px", borderRadius: "8px", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}>
          <Table
            columns={columns}
            dataSource={appointments}
            rowKey="appointmentID"
            pagination={{ pageSize: 10 }}
            locale={locale}
            loading={loading}
            rowClassName={(record, index) => index % 2 === 0 ? 'even-row' : 'odd-row'}
          />
        </div>

        <Modal
          title={<><ExclamationCircleOutlined style={{ color: '#faad14' }} /> Cancel Appointment</>}
          open={cancelModalVisible}
          confirmLoading={cancelLoading}
          onOk={handleCancel}
          onCancel={() => setCancelModalVisible(false)}
          okText="Yes, Cancel"
          cancelText="No"
        >
  <p style={{ fontSize: '16px' }}>Are you sure you want to cancel this appointment? This action cannot be undone.</p>
  </Modal>

        <Modal
          title={<><StarOutlined style={{ color: '#1890ff' }} /> View your feedback</>}
          confirmLoading={feedbackLoading}
          open={feedbackModalVisible}
          onCancel={() => {
            setFeedbackModalVisible(false);
            setSelectedAppointmentId(null);
            setFeedback("");
            setStarRating(0);
          }}
          onOk={handleUpdateFeedback}
          okText="Update"
          cancelText="Cancel"
        >
          <Form layout="vertical">
            <Form.Item label="Feedback">
              <Input.TextArea
                value={feedback}
                onChange={(e) => setFeedback(e.target.value)}
              />
            </Form.Item>
            <Form.Item label="Rate Appointment">
              <Rate
                value={starRating}
                onChange={(value) => setStarRating(value)}
              />
            </Form.Item>
          </Form>
        </Modal>
      </div>
    </div>
  );
};

export default History;
