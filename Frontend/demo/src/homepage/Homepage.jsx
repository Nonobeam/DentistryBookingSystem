import React, { useEffect } from "react";
import NavBar from "./Nav";
import styled from 'styled-components';
import "antd/dist/reset.css";
import { Layout, Button, Row, Col, Card, Typography, notification } from "antd";
import {
  SmileOutlined,
  ProjectOutlined,
  FileDoneOutlined,
  MailOutlined,
  FacebookOutlined,
  TwitterOutlined,
  YoutubeOutlined,
  LinkedinOutlined,
} from "@ant-design/icons";
import axios from "axios";

const StyledFooter = styled(Layout.Footer)`
  text-align: center;
  background-color: #1890ff;
  color: white;
  padding: 40px 0;
`;

const { Title, Paragraph } = Typography;

const Homepage = () => {
  useEffect(() => {
    const checkFeedbacks = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/user/appointment-feedback", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.data && response.data.length > 0) {
          notification.info({
            message: "Feedback Reminder",
            description: (
              <span>
                You have appointments to provide feedback on. Please click <a href="/appointment-feedback">here</a> to give your feedback.
              </span>
            ),
            duration: 5, // Thời gian hiển thị thông báo (tính bằng giây)
          });
        }
      } catch (error) {
        console.error("Failed to fetch feedback data", error);
      }
    };

    checkFeedbacks();
  }, []);

  return (
    <>
    <Layout className="layout">
      <NavBar />

      <Layout.Content style={{ padding: "0 50px" }}>
        <div className="hero-section" style={{ padding: "50px 0", textAlign: "center" }}>
          <Row gutter={[16, 16]} align="middle">
            <Col xs={24} md={12}>
              <Title>Welcome to Sunflower Dentistry</Title>
              <Paragraph>
                Where your smile matters most. Our expert team provides personalized dental care in a friendly environment.
              </Paragraph>
              <Button type="primary" style={{ marginRight: "10px" }} href="/booking">
                Schedule Your Appointment
              </Button>
            </Col>
            <Col xs={24} md={12}>
              <img
                src="https://mytowncenterdental.com/wp-content/uploads/2023/10/dentist-in-cedar-park-tx-town-center-dental-cedar-park.jpg"
                alt="Dentistry illustration"
                style={{ width: "100%" }}
              />
            </Col>
          </Row>
        </div>

        <div className="features-section" style={{ padding: "50px 0", background: "#f0f2f5" }}>
          <Title level={2} style={{ textAlign: "center" }}>
            What Makes Sunflower Dentistry Different?
          </Title>
          <Paragraph style={{ textAlign: "center", maxWidth: "600px", margin: "0 auto" }}>
            At Sunflower Dentistry, we stand out with our patient-centered approach, state-of-the-art technology, and a warm, welcoming atmosphere. Our experienced team is dedicated to personalized care, ensuring every visit is comfortable and tailored to your needs.
          </Paragraph>
          <Row gutter={[16, 16]} justify="center" style={{ marginTop: "30px" }}>
            <Col xs={24} sm={12} md={6}>
              <Card>
                <SmileOutlined style={{ fontSize: "34px", color: "#1890ff" }} />
                <Title level={3}>25k+</Title>
                <Paragraph>Happy Customers</Paragraph>
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card>
                <ProjectOutlined style={{ fontSize: "34px", color: "#1890ff" }} />
                <Title level={3}>6+</Title>
                <Paragraph>Years of Experience</Paragraph>
              </Card>
            </Col>
            
            <Col xs={24} sm={12} md={6}>
              <Card>
                <FileDoneOutlined style={{ fontSize: "34px", color: "#1890ff" }} />
                <Title level={3}>80+</Title>
                <Paragraph>Available Services</Paragraph>
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card>
                <MailOutlined style={{ fontSize: "34px", color: "#1890ff" }} />
                <Title level={3}>1000+</Title>
                <Paragraph>Registered Clients</Paragraph>
              </Card>
            </Col>
          </Row>
        </div>
        <div className="video-section" style={{ padding: "50px 0", background: "#f0f2f5" }}>
          <Title level={2} style={{ textAlign: "center" }}>
            Why Do You Need a Dentist?
          </Title>
          <Paragraph style={{ textAlign: "center", maxWidth: "600px", margin: "0 auto" }}>
            Regular dental visits are essential for maintaining oral health, preventing tooth decay, and catching issues early. A dentist ensures your smile stays healthy and bright through professional cleanings and expert care.
          </Paragraph>
          <div style={{ textAlign: "center", marginTop: "30px" }}>
            <img
              src="https://fermeliadental.com/wp-content/uploads/2019/05/benefits-of-regular-dental-visits.jpeg"
              alt="Dentist"
              style={{ width: "100%", maxWidth: "600px" }}
            />
          </div>
        </div>

        <div className="dentists-section" style={{ padding: "50px 0" }}>
          <Title level={2} style={{ textAlign: "center" }}>
            Meet Our Experienced Dentists
          </Title>
          <Row gutter={[16, 16]} justify="center" style={{ marginTop: "30px" }}>
            <Col xs={24} sm={12} md={6}>
              <Card cover={<img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />}>
                <Card.Meta title="Vo Ngoc Bao Thu" description="Wisdom Teeth Specialist" />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card cover={<img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />}>
                <Card.Meta title="Nguyen Huu Phuc" description="Cavity Treatment Specialist" />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card cover={<img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />}>
                <Card.Meta title="Ariana Grande" description="Teeth Whitening Specialist" />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card cover={<img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />}>
                <Card.Meta title="Mono Hoang" description="Tooth Replacement Specialist" />
              </Card>
            </Col>
          </Row>
        </div>
      </Layout.Content>

      <StyledFooter>
        <Layout.Footer style={{ textAlign: "center", backgroundColor: "#1890ff" }}>
          <Row gutter={[16, 16]}>
            <Col xs={24} sm={16} md={8}>
              <Title level={4}>About Us</Title>
              <Paragraph>Sunflower Dentistry is dedicated to providing top-notch dental care to our community.</Paragraph>
            </Col>
            <Col xs={24} sm={16} md={8}>
              <Title level={4}>Contact</Title>
              <Paragraph>123 Dental St.</Paragraph>
              <Paragraph>Sun City, TX 12345</Paragraph>
              <Paragraph>info@sunflowerdentistry.com</Paragraph>
            </Col>
            <Col xs={24} sm={16} md={8}>
              <Title level={4}>Follow Us</Title>
              <Paragraph>
                <FacebookOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                <TwitterOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                <YoutubeOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
                <LinkedinOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
              </Paragraph>
            </Col>
          </Row>
        </Layout.Footer>
      </StyledFooter>
    </Layout>
    </>
  );
};

export default Homepage;
