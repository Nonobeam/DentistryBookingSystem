import React, { useEffect, useState } from "react";
import axios from "axios";
import { Table, Button, Modal, Form, DatePicker, Select, Spin, message } from "antd";
import { useNavigate } from "react-router-dom";
import moment from "moment";
import NavBar from "./Nav";

const { Option } = Select;

const History = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filterDate, setFilterDate] = useState(null);
  const [filterStatus, setFilterStatus] = useState(null);
  const [cancelModalVisible, setCancelModalVisible] = useState(false);
  const [selectedAppointmentId, setSelectedAppointmentId] = useState(null);
  const history = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, [filterDate, filterStatus]);

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
      await axios.post(`http://localhost:8080/user/cancel-appointment`, { appointmentID: selectedAppointmentId });
      message.success("Appointment cancelled successfully");
      fetchAppointments();
      setCancelModalVisible(false);
    } catch (error) {
      message.error("Failed to cancel appointment");
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
        if (status === 1) return "Cancelled";
        const appointmentDate = moment(record.date);
        const currentDate = moment();
        if (appointmentDate.isBefore(currentDate, "day")) return "Finished";
        return "Upcoming";
      },
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => {
        const appointmentDate = moment(record.date);
        const currentDate = moment();
        if (record.status === 0 && appointmentDate.isSameOrAfter(currentDate, "day")) {
          return (
            <Button
              type="link"
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

  return (

    <div>
        <NavBar />
      <h1>Appointment History</h1>
      <Form layout="inline" style={{ marginBottom: 16 }}>
        <Form.Item label="Date">
          <DatePicker onChange={(date) => setFilterDate(date)} />
        </Form.Item>
        <Form.Item label="Status">
          <Select style={{ width: 120 }} onChange={(value) => setFilterStatus(value)} allowClear>
            <Option value={0}>Active</Option>
            <Option value={1}>Cancelled</Option>
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchAppointments}>
            Filter
          </Button>
        </Form.Item>
      </Form>
      <Button type="primary" style={{ marginBottom: 16 }} onClick={() => history("/booking")}>
        Make New Appointment
      </Button>
      {loading ? (
        <Spin />
      ) : (
        <Table columns={columns} dataSource={appointments} rowKey="appointmentID" />
      )}

      <Modal
        title="Cancel Appointment"
        open={cancelModalVisible}
        onOk={handleCancel}
        onCancel={() => setCancelModalVisible(false)}
      >
        <p>Are you sure you want to cancel this appointment?</p>
      </Modal>
    </div>
  );
};

export default History;
