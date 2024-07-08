import React, { useState, useEffect } from "react";
import { Layout, DatePicker, Select, Card, Row, Col, Spin, Empty } from "antd";
import { Pie, Bar } from "@ant-design/plots";
import axios from "axios";
import BossSidebar from "./BossSideBar";
import dayjs from "dayjs";

const { Content } = Layout;
const { Option } = Select;

const BossDashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedYear, setSelectedYear] = useState(dayjs().year());
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchDashboardData(selectedDate, selectedYear);
  }, [selectedDate, selectedYear]);

  const fetchDashboardData = async (date, year) => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const params = {};
      if (date) params.date = date.format("YYYY-MM-DD");
      if (year) params.year = year;
      const response = await axios.get(
        "http://localhost:8080/api/v1/boss/dashboard",
        {
          params,
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setDashboardData(response.data);
    } catch (error) {
      console.error("Error fetching dashboard data:", error);
    } finally {
      setLoading(false);
    }
  };

  const generateYearOptions = () => {
    const currentYear = dayjs().year();
    return Array.from({ length: 10 }, (_, index) => currentYear - index);
  };

  const renderPieChart = () => {
    if (selectedDate === null) {
      return <Empty description="Choose a date" />;
    }

    const data = Object.entries(dashboardData.dailyAppointments).map(
      ([key, value]) => ({
        type: key,
        value: value,
      })
    );

    if (data.length === 0) {
      return <Empty description="This date doesn't have any appointments" />;
    }

    const config = {
      appendPadding: 10,
      data,
      angleField: 'value',
      colorField: 'type',
      radius: 0.8,
      label: {
        offset: "-30%",
        content: ({ percent }) => `${(percent * 100).toFixed(0)}%`,
        style: {
          fontSize: 14,
          textAlign: 'center',
        },
      },
      interactions: [{ type: "element-active" }],
    };

    return <Pie {...config} />;
  };

  const renderBarChart = () => {
    if (!dashboardData || !dashboardData.monthlyAppointments) return null;

    const data = [];
    Object.entries(dashboardData.monthlyAppointments).forEach(
      ([clinic, months]) => {
        Object.entries(months).forEach(([month, count]) => {
          data.push({
            clinic,
            month: parseInt(month),
            count,
          });
        });
      }
    );

    const config = {
      data,
      xField: "month",
      yField: "count",
      seriesField: "clinic",
      isGroup: true,
      columnStyle: {
        radius: [20, 20, 0, 0],
      },
    };

    return <Bar {...config} />;
  };

  

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <BossSidebar />
      <Layout style={{ padding: "24px 24px" }}>
        <Content style={{ padding: 24, margin: 0, minHeight: 280 }}>
          <h2>Dashboard</h2>
          <Row gutter={16} style={{ marginBottom: 16 }}>

            <Col span={9}>
              <Card>
                <h3>Total appointments of the month</h3>
                <p>{dashboardData?.totalAppointmentsInMonthNow || 0}</p>
              </Card>
            </Col>
            <Col span={9}>
              <Card>
                <h3>Total appointments of the year</h3>
                <p>{dashboardData?.totalAppointmentsInYearNow || 0}</p>
              </Card>
            </Col>

            <Col span={6}>
              <DatePicker
                value={selectedDate}
                onChange={(date) => setSelectedDate(date)}
                style={{ width: "100%", marginBottom: 32, marginTop: 10}}
              />
              <Select
                value={selectedYear}
                onChange={(year) => setSelectedYear(year)}
                style={{ width: "100%" }}
              >
                {generateYearOptions().map((year) => (
                  <Option key={year} value={year}>
                    {year}
                  </Option>
                ))}
              </Select>
            </Col>
          </Row>

          {loading ? (
            <Spin
              size="medium"
              style={{ display: "block", margin: "20px auto" }}
            />
          ) : (
            <>
              <Row gutter={16}>
                <Col span={12}>
                  <Card title="Daily Appointments">{renderPieChart()}</Card>
                </Col>
                <Col span={12}>
                  <Card title="Monthly Appointments">{renderBarChart()}</Card>
                </Col>
              </Row>
            </>
          )}
        </Content>
      </Layout>
    </Layout>
  );
};

export default BossDashboard;
