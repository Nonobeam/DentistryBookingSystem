import React, { useState, useEffect } from "react";
import { Layout, Card, Row, Col, Typography, Divider, Empty } from "antd";
import axios from "axios";
import { Bar } from "@ant-design/charts";
import ManagerSidebar from "./ManagerSidebar";
import { HomeOutlined, CalendarOutlined } from "@ant-design/icons";

const { Content } = Layout;
const { Title } = Typography;

const ManagerDashboard = () => {
  const [year, setYear] = useState(new Date().getFullYear()); // Set initial year to the current year
  const [dashboardData, setDashboardData] = useState({
    monthlyAppointments: {},
    totalAppointmentsInMonthNow: 0,
    totalAppointmentsInYearNow: 0,
    ratingDentist: {},
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchDashboardData(year);
  }, [year]);

  const fetchDashboardData = async (selectedYear) => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(
        `http://localhost:8080/api/v1/manager/dashboard?year=${selectedYear}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      const data = response.data;
      setDashboardData({
        monthlyAppointments: data.monthlyAppointments || {},
        totalAppointmentsInMonthNow: data.totalAppointmentsInMonthNow || 0,
        totalAppointmentsInYearNow: data.totalAppointmentsInYearNow || 0,
        ratingDentist: data.ratingDentist || {},
      });
    } catch (error) {
      console.error("Error fetching dashboard data:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleYearChange = (selectedYear) => {
    setYear(selectedYear);
  };

  // Generate the last 5 years including the current year
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 5 }, (_, index) => currentYear - index);

  const barData = Object.keys(dashboardData.monthlyAppointments).map((key) => ({
    clinic: key,
    appointments: dashboardData.monthlyAppointments[key]["7"] || 0,
  }));

  const ratingBarData = Object.keys(dashboardData.ratingDentist).map((key) => ({
    dentist: key,
    rating: dashboardData.ratingDentist[key],
  }));

  const barConfig = {
    data: barData,
    xField: "clinic",
    yField: "appointments",
    seriesField: "clinic",
    colorField: "clinic",
    legend: {
      position: "top-left",
    },
    xAxis: {
      label: {
        autoRotate: true,
      },
    },
    barWidthRatio: 0.6,
    color: ["#1890ff"],
  };

  const ratingBarConfig = {
    data: ratingBarData,
    xField: "dentist",
    yField: "rating",
    seriesField: "dentist",
    colorField: "dentist",
    legend: {
      position: "top-left",
    },
    xAxis: {
      label: {
        autoRotate: true,
      },
    },
    barWidthRatio: 0.6,
    color: [
      "#ff4d4f",
      "#ffc107",
      "#52c41a",
      "#1890ff",
      "#fcffe6",
      "#ffffb8",
      "#f5222d",
    ], // Example colors
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <ManagerSidebar />

      <Layout style={{ padding: "24px" }}>
        <Content
          style={{
            padding: "24px",
            margin: 0,
            minHeight: "85vh",
            backgroundColor: "#fff",
          }}
        >
          <Row gutter={[16, 16]}>
            <Col span={24}>
              <Title level={2} style={{ textAlign: "center" }}>
                Manager Dashboard
              </Title>
            </Col>
            <Col span={24}>
              <Row gutter={[16, 16]} justify="center">
                {years.map((item) => (
                  <Col key={item} xs={12} sm={6} md={4} lg={4}>
                    <Card
                      onClick={() => handleYearChange(item)}
                      bordered={false}
                      style={{
                        textAlign: "center",
                        cursor: "pointer",
                        height: "100px",
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: year === item ? "#C6E2FF" : "#e6f7ff",
                        border: "1px solid #d9d9d9",
                        marginBottom: "16px",
                        borderRadius: "8px",
                      }}
                    >
                      <Title level={4} style={{ margin: 0 }}>
                        {item}
                      </Title>
                    </Card>
                  </Col>
                ))}
              </Row>
            </Col>
            <Col span={24}>
              <Row justify="center" gutter={[16, 16]}>
                <Col xs={24} sm={12} md={10} lg={8} xl={6}>
                  <Card
                    bordered={false}
                    style={{
                      backgroundColor: "#fcffe6",
                      textAlign: "center",
                      padding: "16px",
                    }}
                  >
                    <HomeOutlined
                      style={{ fontSize: "24px", color: "#1890ff" }}
                    />
                    <Title level={5}>Today's Month</Title>
                    <Divider style={{ margin: "12px 0" }} />
                    <Title level={4} style={{ margin: 0 }}>
                      {dashboardData.totalAppointmentsInMonthNow}
                    </Title>
                  </Card>
                </Col>
                <Col xs={24} sm={12} md={10} lg={8} xl={6}>
                  <Card
                    bordered={false}
                    style={{
                      backgroundColor: "#ffffb8",
                      textAlign: "center",
                      padding: "16px",
                    }}
                  >
                    <CalendarOutlined
                      style={{ fontSize: "24px", color: "#f5222d" }}
                    />
                    <Title level={5}>Today's Year</Title>
                    <Divider style={{ margin: "12px 0" }} />
                    <Title level={4} style={{ margin: 0 }}>
                      {dashboardData.totalAppointmentsInYearNow}
                    </Title>
                  </Card>
                </Col>
              </Row>
            </Col>
          </Row>
          <Divider style={{ margin: "24px 0" }} />
          <Row gutter={[16, 16]}>
            <Col span={24} md={12}>
              <Card loading={loading}>
                <Title level={4}>Monthly Appointments by Clinic</Title>
                <Divider style={{ margin: "12px 0" }} />
                {barData.length > 0 ? (
                  <Bar {...barConfig} />
                ) : (
                  <Empty description="No data available" />
                )}
              </Card>
            </Col>
            <Col span={24} md={12}>
              <Card loading={loading}>
                <Title level={4}>Rating of Dentists</Title>
                <Divider style={{ margin: "12px 0" }} />
                {ratingBarData.length > 0 ? (
                  <Bar {...ratingBarConfig} />
                ) : (
                  <Empty description="No rating data available" />
                )}
              </Card>
            </Col>
          </Row>
        </Content>
      </Layout>
    </Layout>
  );
};

export default ManagerDashboard;
