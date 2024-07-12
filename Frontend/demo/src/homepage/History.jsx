import { Button, DatePicker, Form, Layout, Modal, Select, Table, Typography, message } from "antd";
import "antd/dist/reset.css";
import axios from "axios";
import moment from "moment";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from 'styled-components';
import NavBar from "./Nav";
const { Title, Paragraph } = Typography;

const { Option } = Select;

const StyledFooter = styled(Layout.Footer)`
  text-align: center;
  background-color: #1890ff;
  color: white;
  padding: 40px 0;
`;
const History = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterDate, setFilterDate] = useState(null);
  const [filterStatus, setFilterStatus] = useState(null);
  const [cancelModalVisible, setCancelModalVisible] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const [sortOrder, setSortOrder] = useState("descend");
  const history = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, [filterDate, filterStatus, sortOrder]);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      let url = `http://localhost:8080/user/appointment-history`;

      if (filterDate || filterStatus !== null) {
        url += `?${filterDate ? `workDate=${filterDate.format("YYYY-MM-DD")}` : ""}${
          filterDate && filterStatus !== null ? "&" : ""
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
  };

  const handleCancel = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.put(
        `http://localhost:8080/user/delete-booking/${selectedAppointmentId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      message.success("Appointment cancelled successfully");
      fetchAppointments();
      setCancelModalVisible(false);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
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
      render: (date) => moment(date).format("DD-MM-YYYY"),
      sorter: (a, b) => moment(a.date).unix() - moment(b.date).unix(),
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
      render: (status, record) => {
        let statusText = "";
        let statusColor = "";
        if (status === 0) {
          statusText = "Cancelled";
          statusColor = "red";
        } else {
          const appointmentDate = moment(record.date);
          const currentDate = moment();
          if (appointmentDate.isBefore(currentDate, "day")) {
            statusText = "Finished";
            statusColor = "green";
          } else {
            statusText = "Upcoming";
            statusColor = "yellow";
          }
        }
        return (
          <span
            style={{
              backgroundColor: statusColor,
              color: "black",
              padding: "4px 8px",
              borderRadius: "10px",
              display: "inline-block",
              minWidth: "70px",
              textAlign: "center"
            }}
          >
            {statusText}
          </span>
        );
      },
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => {
        const appointmentDate = moment(record.date);
        const currentDate = moment();
        if (record.status === 1 && appointmentDate.isSameOrAfter(currentDate, "day")) {
          return (
            <Button
              type="primary"
              danger
              onClick={() => {
                setSelectedAppointmentId(record.appointmentID);
                setCancelModalVisible(true);
              }}
            >
              Cancel
            </Button>
          );
        }
        return null;
      },
    },
  ];

  const locale = {
    emptyText: 'There are no appointments.',
  };

  return (
    <div>
      <NavBar />
      <div style={{ maxWidth: '90%', margin: "0 auto", padding: "20px" }}>
        <h1 style={{ textAlign: "center", marginBottom: "20px" }}>Appointment History</h1>
        <Form layout="inline" style={{ justifyContent: "center", marginBottom: "20px" }}>
          <Form.Item label="Filter by Date">
            <DatePicker onChange={(date) => setFilterDate(date)} />
          </Form.Item>
          <Form.Item label="Filter by Status">
            <Select style={{ width: 150 }} onChange={(value) => setFilterStatus(value)} allowClear>
              <Option value={0}>Cancelled</Option>
              <Option value={1}>Upcoming</Option>
            </Select>
          </Form.Item>
          <Form.Item>
            <Button type="primary" style={{ marginLeft: "10px" }} onClick={() => history("/booking")}>
              New Appointment
            </Button>
          </Form.Item>
        </Form>

        <div style={{ background: "#fff", padding: "20px", borderRadius: "8px", boxShadow: "0 2px 8px rgba(0,0,0,0.1)" }}>
            <Table
              columns={columns}
              dataSource={appointments}
              rowKey="appointmentID"
              pagination={{ pageSize: 10 }}
              locale={locale}
              loading={loading}
            />
          
        </div>

        <Modal
          title="Cancel Appointment"
          open={cancelModalVisible}
          onOk={handleCancel}
          onCancel={() => setCancelModalVisible(false)}
          okText="Yes, Cancel"
          cancelText="No"
        >
          <p>Are you sure you want to cancel this appointment? This action cannot be undone.</p>
        </Modal>
      </div>
    </div>
    
  );
};

export default History;
