import React, { useState, useEffect } from "react";
import { Layout, Card, Row, Col, Typography, Divider, Empty, Select, Spin } from "antd";
import axios from "axios";
import { Bar } from "@ant-design/charts";
import ManagerSidebar from "./ManagerSidebar";
import { HomeOutlined, CalendarOutlined, LineChartOutlined, StarOutlined } from "@ant-design/icons";
import styled from "styled-components";

const { Content } = Layout;
const { Title, Text } = Typography;
const { Option } = Select;

const StyledContent = styled(Content)`
  padding: 24px;
  margin: 0;
  min-height: 85vh;
  background-color: #f0f2f5;
`;

const StyledCard = styled(Card)`
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 12px rgba(0, 0, 0, 0.15);
  }
`;

const IconWrapper = styled.div`
  font-size: 36px;
  margin-bottom: 16px;
`;

const AnimatedNumber = styled.span`
  display: inline-block;
  animation: countUp 1s ease-out forwards;
  opacity: 0;

  @keyframes countUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
`;

const ManagerDashboard = () => {
  const [year, setYear] = useState(new Date().getFullYear());
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
    legend: { position: "top-left" },
    xAxis: { label: { autoRotate: true } },
    barWidthRatio: 0.6,
    color: ["#1890ff", "#13c2c2", "#52c41a", "#faad14", "#f5222d"],
    animation: { appear: { animation: "fade-in" } },
  };

  const ratingBarConfig = {
    data: ratingBarData,
    xField: "dentist",
    yField: "rating",
    seriesField: "dentist",
    colorField: "dentist",
    legend: { position: "top-left" },
    xAxis: { label: { autoRotate: true } },
    barWidthRatio: 0.6,
    color: ["#1890ff", "#13c2c2", "#52c41a", "#faad14", "#f5222d"],
    animation: { appear: { animation: "grow-in-y" } },
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <ManagerSidebar />
      <StyledContent>
        <Spin spinning={loading}>
          <Row gutter={[24, 24]}>
            <Col span={24}>
              <Title level={2} style={{ textAlign: "center", marginBottom: 32 }}>
                Manager Dashboard
              </Title>
            </Col>
            <Col span={24}>
              <Row justify="center" style={{ marginBottom: 24 }}>
                <Col>
                  <Select
                    value={year}
                    onChange={handleYearChange}
                    style={{ width: 120 }}
                  >
                    {years.map((item) => (
                      <Option key={item} value={item}>
                        {item}
                      </Option>
                    ))}
                  </Select>
                </Col>
              </Row>
            </Col>
            <Col xs={24} sm={12} md={12} lg={12} xl={6}>
              <StyledCard>
                <IconWrapper>
                  <HomeOutlined style={{ color: "#1890ff" }} />
                </IconWrapper>
                <Title level={4}>This Month</Title>
                <AnimatedNumber>
                  <Title level={2}>{dashboardData.totalAppointmentsInMonthNow}</Title>
                </AnimatedNumber>
                <Text type="secondary">Appointments</Text>
              </StyledCard>
            </Col>
            <Col xs={24} sm={12} md={12} lg={12} xl={6}>
              <StyledCard>
                <IconWrapper>
                  <CalendarOutlined style={{ color: "#52c41a" }} />
                </IconWrapper>
                <Title level={4}>This Year</Title>
                <AnimatedNumber>
                  <Title level={2}>{dashboardData.totalAppointmentsInYearNow}</Title>
                </AnimatedNumber>
                <Text type="secondary">Appointments</Text>
              </StyledCard>
            </Col>
            <Col span={24} md={12}>
              <StyledCard>
                <IconWrapper>
                  <LineChartOutlined style={{ color: "#faad14" }} />
                </IconWrapper>
                <Title level={4}>Monthly Appointments by Clinic</Title>
                <Divider />
                {barData.length > 0 ? (
                  <Bar {...barConfig} />
                ) : (
                  <Empty description="No data available" />
                )}
              </StyledCard>
            </Col>
            <Col span={24} md={12}>
              <StyledCard>
                <IconWrapper>
                  <StarOutlined style={{ color: "#f5222d" }} />
                </IconWrapper>
                <Title level={4}>Rating of Dentists</Title>
                <Divider />
                {ratingBarData.length > 0 ? (
                  <Bar {...ratingBarConfig} />
                ) : (
                  <Empty description="No rating data available" />
                )}
              </StyledCard>
            </Col>
          </Row>
        </Spin>
      </StyledContent>
    </Layout>
  );
};

export default ManagerDashboard;