import React, { useState, useEffect } from "react";
import axios from "axios";
import { Layout, Calendar, DatePicker, Spin, message } from "antd";
import dayjs from "dayjs";
import Sidebar from "./Sidebar";
import "./style.css";

const { Header, Content } = Layout;
const { RangePicker } = DatePicker;

const DentistSchedule = () => {
  const initialStartDate = dayjs();
  const initialEndDate = dayjs().add(30, "days");

  const [loading, setLoading] = useState(true);
  const [scheduleData, setScheduleData] = useState({});
  const [startDate, setStartDate] = useState(initialStartDate);
  const [endDate, setEndDate] = useState(initialEndDate);

  useEffect(() => {
    fetchSchedule(initialStartDate, initialEndDate);
  }, []);

  const fetchSchedule = async (start, end) => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const numDays = end.diff(start, 'days');
      console.log(numDays);
      const response = await axios.get(`http://localhost:8080/api/v1/dentist/weekSchedule/${start.format('YYYY-MM-DD')}?numDay=${numDays}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setScheduleData(response.data);
    } catch (error) {
      message.error(error.response?.data || "An error occurred");
    }
    setLoading(false);
  };

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
            <span>{dayjs(item.time, "HH:mm:ss").format("h:mm A")}</span> - <span>{item.customerName || "N/A"}</span> ({item.serviceName || "N/A"})
          </li>
        ))}
      </ul>
    );
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sidebar />
      <Layout className="site-layout">
        <Header className="site-layout-sub-header-background" style={{ padding: 0 }} />

        <Content style={{ margin: "0 16px" }}>
          <div style={{ padding: 24, background: "#fff", minHeight: 360 }}>
            <h1>Dentist Schedule</h1>
            <RangePicker
              onChange={onRangeChange}
              defaultValue={[initialStartDate, initialEndDate]}
              style={{ marginBottom: 16 }}
            />
            {loading ? (
              <Spin />
            ) : (
              <Calendar
                cellRender={dateCellRender}
                disabledDate={(current) => {
                  return current && (current < startDate || current > endDate);
                }}
                validRange={[startDate, endDate]}
              />
            )}
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default DentistSchedule;
