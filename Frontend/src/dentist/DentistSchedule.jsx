import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import { Layout, Calendar, DatePicker, Spin, message, Badge, Card, Typography, Row, Col, Statistic } from "antd";
import { CalendarOutlined, ClockCircleOutlined, UserOutlined, MedicineBoxOutlined } from '@ant-design/icons';
import dayjs from "dayjs";
import Sidebar from "./Sidebar";
import "./style.css";

const { Header, Content } = Layout;
const { RangePicker } = DatePicker;
const { Title, Text } = Typography;

const DentistSchedule = () => {
  const initialStartDate = dayjs();
  const initialEndDate = dayjs().add(30, "days");

  const [loading, setLoading] = useState(false);
  const [scheduleData, setScheduleData] = useState({});
  const [startDate, setStartDate] = useState(initialStartDate);
  const [endDate, setEndDate] = useState(initialEndDate);
  const [clinicInfo, setClinicInfo] = useState("");
  const [totalAppointments, setTotalAppointments] = useState(0);
  
  const fetchClinicInfo = useCallback(async () => {
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
  }, []);

  const fetchSchedule = useCallback(async (start, end) => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token");
      const numDays = end.diff(start, 'days');
      const response = await axios.get(`http://localhost:8080/api/v1/dentist/weekSchedule/${start.format('YYYY-MM-DD')}?numDay=${numDays}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setScheduleData(response.data);
      setTotalAppointments(Object.values(response.data).flat().length);
      setLoading(false);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
  }, []);

  useEffect(() => {
    fetchSchedule(initialStartDate, initialEndDate);
    fetchClinicInfo(); 
  }, [fetchClinicInfo, fetchSchedule]);

  const onRangeChange = (dates) => {
    setStartDate(dates[0]);
    setEndDate(dates[1].add(1, "days"));
    fetchSchedule(dates[0], dates[1]);
  };

  const dateCellRender = (value) => {
    const formattedDate = value.format("YYYY-MM-DD");
    const dayData = scheduleData[formattedDate] || [];
    return (
      <ul className="events">
        {dayData.map(item => (
          <li key={item.id}>
                <Badge 
            status={
              item.status === 1 
                ? 'processing' 
                : item.status === 2 
                  ? 'success' 
                  : 'error'
            } 
            text={
                <Text ellipsis={true} style={{ width: '100%' }}>
                  <ClockCircleOutlined /> {dayjs(item.time, "HH:mm:ss").format("h:mm A")} - <UserOutlined /> {item.customerName || "N/A"} <MedicineBoxOutlined /> {item.serviceName || "N/A"}
                </Text>
            } 
          />
        </li>
        ))}
      </ul>
    );
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sidebar />
      <Layout className="site-layout">
      <Header
          className="site-layout-sub-header-background"
          style={{ 
            padding: 0,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            height: '64px',
          }}
        >
          <Title level={3} style={{ color: 'white', margin: 0 }}>
            {clinicInfo}
          </Title>
        </Header>
        <Content style={{ margin: "24px 16px" }}>
          <Card>
            <Row gutter={16} style={{ marginBottom: 24 }}>
              <Col span={12}>
                <Title level={2}>
                  <CalendarOutlined /> Dentist Schedule
                </Title>
              </Col>
              <Col span={12} style={{ textAlign: 'right' }}>
                <Statistic 
                  title="Total Appointments" 
                  value={totalAppointments} 
                  style={{ float: 'right' }}
                />
              </Col>
            </Row>
            <RangePicker
              onChange={onRangeChange}
              format={"DD-MM-YYYY"}
              defaultValue={[initialStartDate, initialEndDate]}
              style={{ marginBottom: 16, width: '100%' }}
            />
            {loading ? (
              <div style={{ textAlign: 'center', margin: '50px 0' }}>
                <Spin size="large" />
              </div>
            ) : (
              <Calendar
                cellRender={dateCellRender}
                disabledDate={(current) => {
                  return current && (current < startDate || current > endDate);
                }}
                validRange={[startDate, endDate]}
              />
            )}
          </Card>
        </Content>
      </Layout>
    </Layout>
  );
};

export default DentistSchedule;