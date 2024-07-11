import React from "react";
import NavBar from "./Nav";
import styled from 'styled-components';

import Map from "./Map";
import "antd/dist/reset.css";
import { Layout, Menu, Button, Row, Col, Card, Typography } from "antd";
import {
  UserOutlined,
  SmileOutlined,
  ProjectOutlined,
  FileDoneOutlined,
  MailOutlined,
  FacebookOutlined,
  TwitterOutlined,
  YoutubeOutlined,
  LinkedinOutlined,
} from "@ant-design/icons";
const StyledFooter = styled(Layout.Footer)`
  text-align: center;
  background-color: #1890ff;
  color: white;
  padding: 40px 0;
`;
const { Title, Paragraph } = Typography;

const locations = [
  { lat: 10.762622, lng: 106.660172, name: "Branch 1" },
  { lat: 10.762622, lng: 106.680172, name: "Branch 2" },
];

const Homepage = () => {
  return (
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
              <Button type="primary" style={{ marginRight: "10px" }}>
                Schedule Your Appointment
              </Button>
              <Button>Need Advice?</Button>
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

        <div className="branches-section" style={{ padding: "50px 0" }}>
          <Row gutter={[16, 16]}>
            <Col xs={24} md={12}>
              <Map locations={locations} />
            </Col>
            <Col xs={24} md={12}>
              <Title level={2}>Our Branches</Title>
              <Paragraph>
                Visit any of our branches for top-quality dental care. We are located at multiple convenient locations to serve you better.
              </Paragraph>
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
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12} md={6}>
            <Title level={4} style={{ color: 'white' }}>About Us</Title>
            <Paragraph style={{ color: 'white' }}>Our Mission</Paragraph>
            <Paragraph style={{ color: 'white' }}>Our Team</Paragraph>
            <Paragraph style={{ color: 'white' }}>Testimonials</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4} style={{ color: 'white' }}>Services</Title>
            <Paragraph style={{ color: 'white' }}>General Dentistry</Paragraph>
            <Paragraph style={{ color: 'white' }}>Cosmetic Dentistry</Paragraph>
            <Paragraph style={{ color: 'white' }}>Orthodontics</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4} style={{ color: 'white' }}>Contact</Title>
            <Paragraph style={{ color: 'white' }}>Location</Paragraph>
            <Paragraph style={{ color: 'white' }}>Phone</Paragraph>
            <Paragraph style={{ color: 'white' }}>Email</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4} style={{ color: 'white' }}>Follow Us</Title>
            <Paragraph>
              <FacebookOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
              <TwitterOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
              <YoutubeOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
              <LinkedinOutlined style={{ fontSize: '24px', margin: '0 10px', color: 'white' }} />
            </Paragraph>
          </Col>
        </Row>
      </StyledFooter>
    </Layout>
    
  );
};

export default Homepage;
