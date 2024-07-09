import React, { useEffect, useState } from "react";
import axios from "axios";
import { Layout, Table, Button, message, Spin } from "antd";
import moment from "moment";
import Sidebar from "./Sidebar";
import { useParams } from "react-router-dom";

const { Header, Content } = Layout;

const PatientInfo = () => {
  const { customerID } = useParams();
  const [patientInfo, setPatientInfo] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [reminderModalVisible, setReminderModalVisible] = useState(false);
  const [reminderMessage, setReminderMessage] = useState("");

  useEffect(() => {
    fetchPatientInfo();
  }, []);

  const fetchPatientInfo = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `http://localhost:8080/api/v1/dentist/customer/${customerID}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      setPatientInfo(response.data.userDTO);
      setAppointments(response.data.appointment);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
    setLoading(false);
  };


  const columns = [
    {
        title: "Patient Name",
        dataIndex: "user",
        key: "user",
        render: (_, record) => {
          const patientName = record.dependent ? `Customer: ${record.user}, Dependent: ${record.dependent}` : `Customer: ${record.user}`;
          return (
            <>{patientName}</> 
          );
        },
      },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
      render: (date) => moment(date).format("DD-MM-YYYY"),
      sorter: (a, b) => moment(a.date).unix() - moment(b.date).unix(),
      defaultSortOrder: "ascend",
    },
    {
      title: "Time",
      dataIndex: "timeSlot",
      key: "timeSlot",
      render: (timeSlot) => timeSlot,
      sorter: (a, b) => moment(a.timeSlot, "HH:mm:ss").unix() - moment(b.timeSlot, "HH:mm:ss").unix(),
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
      render: (status) => {
        let statusText = "";
        let statusColor = "";
        if (status === 0) {
          statusText = "Cancelled";
          statusColor = "red";
        } else if (status === 2) {
          statusText = "Completed";
          statusColor = "green";
        } else if (status === 1) {
          statusText = "Upcoming";
          statusColor = "yellow";
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
    
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sidebar />
      <Layout className="site-layout">
        <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />

        <Content style={{ margin: "0 16px" }}>
          <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
            <h1>Patient Information</h1>

            {loading ? (
              <Spin />
            ) : (
              <>
                <div style={{ marginBottom: 16 }}>
                  <p><b>Name:</b> {patientInfo?.name}</p>
                  <p><b>Phone:</b> {patientInfo?.phone}</p>
                  <p><b>Email:</b> {patientInfo?.mail}</p>
                  <p><b>Birthday:</b> {moment(patientInfo?.birthday).format("DD-MM-YYYY")}</p>
                  <p><b>Status:</b> {patientInfo?.status === 0 ? "Inactive" : "Active"}</p>
                </div>

                <h2>Appointment History</h2>
                <Table
                  columns={columns}
                  dataSource={appointments}
                  rowKey="appointmentId"
                  pagination={{ pageSize: 10 }}
                  style={{ marginBottom: 20 }}
                />

                
              </>
            )}
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default PatientInfo;
