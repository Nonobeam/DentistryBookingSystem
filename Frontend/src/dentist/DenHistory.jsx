import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { Button, Input, Layout, Modal, Select, Table, message, Typography, Card, Row, Col, Statistic, Tag } from "antd";
import { HistoryOutlined, UserOutlined, CalendarOutlined, ClockCircleOutlined, MedicineBoxOutlined, TeamOutlined } from '@ant-design/icons';
import dayjs from "dayjs";
import { useNavigate } from "react-router-dom";
import Sidebar from "./Sidebar";

const { Option } = Select;
const { Header, Content } = Layout;
const { Title, Text } = Typography;
const { Search } = Input;

const DenHistory = () => {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editLoading, setEditLoading] = useState(false);
  const [statusModalVisible, setStatusModalVisible] = useState(false);
  const [selectedAppointment, setSelectedAppointment] = useState(null);
  const [newStatus, setNewStatus] = useState(null);
  const filterDate = null;
  const [filterName, setFilterName] = useState(""); 
  const [clinicInfo, setClinicInfo] = useState(""); 
  const navigate = useNavigate();



  const fetchAppointments = useCallback ( async () => {
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
  }, [filterDate, filterName]);

  const fetchClinicInfo = useCallback ( async () => {
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
  },[]);

  useEffect(() => {
    fetchAppointments();
    fetchClinicInfo(); 
  }, [fetchAppointments,fetchClinicInfo, filterDate, filterName]);

  const handleNameChange = (e) => {
    setFilterName(e.target.value);
  };

  const handleChangeStatus = async () => {
    setEditLoading(true);
    const appointmentDateTime = dayjs(`${selectedAppointment.date} ${selectedAppointment.timeSlot}`);
    const now = dayjs();

    if (newStatus === 2 && appointmentDateTime.isAfter(now)) {
      message.error("Cannot mark future appointments as completed");
      return;
    }

    try {
      const token = localStorage.getItem("token");
      await axios.patch(
        `http://localhost:8080/api/v1/dentist/appointment-history/${selectedAppointment.appointmentId}?status=${newStatus}`, [],
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      setEditLoading(false);
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
        const patientName = record.dependent ? `${record.user} (${record.dependent})` : record.user;
        return (
          <Button type="link" onClick={() => navigate(`/dentist/patient/${record.customerID}`)}>
            <UserOutlined /> {patientName}
          </Button>
        );
      },
    },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
      render: (date) => (
        <span>
          <CalendarOutlined /> {dayjs(date).format("DD-MM-YYYY")}
        </span>
      ),
      sorter: (a, b) => dayjs(a.date).unix() - dayjs(b.date).unix(),
      defaultSortOrder: "ascend",
    },
    {
      title: "Time",
      dataIndex: "timeSlot",
      key: "timeSlot",
      render: (timeSlot) => (
        <span>
          <ClockCircleOutlined /> {timeSlot}
        </span>
      ),
      sorter: (a, b) => dayjs(a.timeSlot, "HH:mm:ss").unix() - dayjs(b.timeSlot, "HH:mm:ss").unix(),
      defaultSortOrder: "descend",
    },
    {
      title: "Service",
      dataIndex: "services",
      key: "services",
      render: (services) => (
        <span>
          <MedicineBoxOutlined /> {services}
        </span>
      ),
    },
    {
      title: "Dentist",
      dataIndex: "dentist",
      key: "dentist",
      render: (dentist) => (
        <span>
          <TeamOutlined /> {dentist}
        </span>
      ),
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      render: (status) => {
        let color = 'default';
        let text = 'Unknown';
        if (status === 0) {
          color = 'red';
          text = 'Cancelled';
          } else if (status === 1) {
          color = 'orange';
          text = 'Upcoming';
        } else if (status === 2) {
          color = 'green';
          text = 'Completed';
        }
        return <Tag color={color}>{text}</Tag>;
      },
    },
    {
      title: "Action",
      key: "action",
      render: (_, record) => {
        if (record.status !== 0) {
          return (
            <Button
              type="primary"
              size="small"
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

        <Content style={{ margin: "24px 16px" }}>
          <Card>
            <Row gutter={[16, 16]} align="middle">
              <Col xs={24} sm={12}>
                <Title level={2}>
                  <HistoryOutlined /> Appointment History
                </Title>
              </Col>
              <Col xs={24} sm={12} style={{ textAlign: 'right' }}>
                <Statistic title="Total Appointments" value={appointments.length} />
              </Col>
            </Row>

            {/* <Row gutter={[16, 16]} style={{ marginTop: 16, marginBottom: 16 }}>
              <Col xs={24} sm={12} md={8} lg={6}>
                <Search
                  placeholder="Search by patient name"
                  allowClear
                  enterButton="Search"
                  size="large"
                  onSearch={handleNameChange}
                />
              </Col>
            </Row> */}

            <Table
              columns={columns}
              dataSource={appointments}
              rowKey="appointmentId"
              pagination={{ pageSize: 10 }}
              loading={loading}
            />

            <Modal
              title="Change Appointment Status"
              open={statusModalVisible}
              onOk={handleChangeStatus}
              onCancel={() => setStatusModalVisible(false)}
              confirmLoading={editLoading}
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
          </Card>
        </Content>
      </Layout>
    </Layout>
  );
};

export default DenHistory;
