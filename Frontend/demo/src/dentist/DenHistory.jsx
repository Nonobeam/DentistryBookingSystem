import { Button, DatePicker, Input, Layout, Modal, Select, Spin, Table, message } from "antd";
import axios from "axios";
import dayjs from "dayjs";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; 
import Sidebar from "./Sidebar";

const { Option } = Select;
const { Header, Content } = Layout;
const { RangePicker } = DatePicker;

const DenHistory = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusModalVisible, setStatusModalVisible] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [newStatus, setNewStatus] = useState(null);
  const [filterDate, setFilterDate] = useState(null); 
  const [filterName, setFilterName] = useState(""); 
  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, [filterDate, filterName]);

  const fetchAppointments = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      let apiUrl = `http://localhost:8080/api/v1/dentist/appointment-history/?`;
      
      if (filterDate) {
        const formattedDate = dayjs(filterDate).format("YYYY-MM-DD");
        apiUrl += `date=${formattedDate}&`;
      }

      if (filterName) {
        apiUrl += `name=${filterName}`;
      }

      const response = await axios.get(apiUrl, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setAppointments(response.data);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
      console.error(error);
    }
    setLoading(false);
  };


  const handleNameChange = (e) => {
    setFilterName(e.target.value);
  };

  const handleChangeStatus = async () => {
    try {
      const token = localStorage.getItem("token");
      await axios.put(
        `http://localhost:8080/api/v1/dentist/appointment-history/${selectedAppointment.appointmentId}?status=${newStatus}`,[],
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      message.success("Appointment status updated successfully");
      fetchAppointments();
      setStatusModalVisible(false);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  };

  const columns = [
    {
      title: "Patient Name",
      dataIndex: "user",
      key: "user",
      render: (_, record) => {
        const patientName = record.dependent ? `Customer: ${record.user}, Dependent: ${record.dependent}` : `Customer: ${record.user}`;
        return (
          <a onClick={() => navigate(`/dentist/patient/${record.customerID}`)}>
            {patientName}
          </a>
        );
      },
    },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
      render: (date) => dayjs(date).format("DD-MM-YYYY"),
      sorter: (a, b) => dayjs(a.date).unix() - dayjs(b.date).unix(),
      defaultSortOrder: "ascend",
    },
    {
      title: "Time",
      dataIndex: "timeSlot",
      key: "timeSlot",
      render: (timeSlot) => timeSlot,
      sorter: (a, b) => dayjs(a.timeSlot, "HH:mm:ss").unix() - dayjs(b.timeSlot, "HH:mm:ss").unix(),
      defaultSortOrder: "descend",
    },
    {
      title: "Service",
      dataIndex: "services",
      key: "services",
      render: (services) => services,
    },
    {
      title: "Dentist",
      dataIndex: "dentist",
      key: "dentist",
      render: (dentist) => dentist,
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
          if (status === 2) {
            statusText = "Completed";
            statusColor = "green";
          } else if (status === 1) {
            statusText = "Upcoming";
            statusColor = "yellow";
          } else if (status === 0){
            statusText = "Cancelled";
            statusColor = "red";
          }
        }
        return (
          <span
            style={{
              backgroundColor: statusColor,
              color: "black",
              padding: "4px 8px",
              borderRadius: "18px",
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
      title: "",
      key: "action",
      render: (_, record) => {
        if (record.status !== 0) {
          return (
            <Button
              type="primary"
              onClick={() => {
                setSelectedAppointment(record);
                setStatusModalVisible(true);
              }}
            >
              Change
            </Button>
          );
        }
        return null;
      },
    },
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sidebar />
      <Layout className="site-layout">
      <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />

        <Content style={{ margin: "0 16px" }}>
          <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
        <h1>Appointment History</h1>

            <div style={{ marginBottom: 16 }}>
              <Input placeholder="Patient Name" value={filterName} onChange={handleNameChange} style={{ marginLeft: 10, width: 200 }} />
            </div>

       
          <Table
            columns={columns}
            dataSource={appointments}
            rowKey="appointmentId"
            pagination={{ pageSize: 10 }}
            style={{ marginBottom: 20 }}
            loading={loading}
          />
        
        <Modal
          title="Change Appointment Status"
              open={statusModalVisible}
              onOk={handleChangeStatus}
          onCancel={() => setStatusModalVisible(false)}
        >
          <p>Change status for appointment with {selectedAppointment?.user}:</p>
              <Select
                defaultValue={selectedAppointment?.status}
                onChange={(value) => setNewStatus(value)}
                style={{ width: '100%' }}
              >
            <Option value={1}>Upcoming</Option>
            <Option value={2}>Completed</Option>
          </Select>
        </Modal>
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default DenHistory;
