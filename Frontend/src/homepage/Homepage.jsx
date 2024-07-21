import React, { useState, useEffect } from "react";
import NavBar from "./Nav";
import styled from 'styled-components';
import "antd/dist/reset.css";
import { Layout, Button, Row, Col, Card, Typography, notification, Carousel, Select } from "antd";
import {
  SmileOutlined,
  ProjectOutlined,
  FileDoneOutlined,
  MailOutlined,
  CalendarOutlined,
  EnvironmentOutlined,
  PhoneOutlined,
  ClockCircleOutlined,
} from "@ant-design/icons";
import axios from "axios";

const { Title, Paragraph } = Typography;
const { Option } = Select;

const PageContainer = styled(Layout)`
  font-family: 'Poppins', sans-serif;
`;

const HeroSection = styled.div`
  background: url('https://images.unsplash.com/photo-1629909613654-28e377c37b09?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80') center/cover no-repeat;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
  }
`;

const HeroContent = styled.div`
  text-align: center;
  color: white;
  z-index: 1;
`;

const StyledContent = styled(Layout.Content)`
  padding: 50px;
  @media (max-width: 768px) {
    padding: 30px;
  }
`;

const FeatureCard = styled(Card)`
  text-align: center;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  overflow: hidden;

  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 30px rgba(0,0,0,0.2);
  }
`;

const DentistCard = styled(Card)`
  text-align: center;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0,0,0,0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 30px rgba(0,0,0,0.2);
  }

  .ant-card-cover img {
    height: 300px;
    object-fit: cover;
  }
`;

const StyledCarousel = styled(Carousel)`
  .slick-slide {
    height: 500px;
    overflow: hidden;
  }

  .slick-slide img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const MapSection = styled.div`
  background-color: #f0f2f5;
  padding: 50px 0;
`;

const StyledFooter = styled(Layout.Footer)`
  background-color: #1976d2;
  color: white;
  padding: 20px 0;
`;



const Homepage = () => {
  const [selectedBranch, setSelectedBranch] = useState('branch1');

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
            duration: 5,
          });
        }
      } catch (error) {
        console.error("Failed to fetch feedback data", error);
      }
    };

    checkFeedbacks();
  }, []);

  const handleBranchChange = (value) => {
    setSelectedBranch(value);
  };


  const branches = {
    branch1: { name: "Tôn Thất Thuyết Clinic", phone: "(028) 837 088", address: "123 Main St, City, Country", map: "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.096949065736!2d105.78009291476343!3d21.028805785998236!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135ab4cd0c66f05%3A0xea31563511af2e54!2zxJDhuqFpIGjhu41jIFF14buRYyBnaWEgSMOgIE7hu5lp!5e0!3m2!1svi!2s!4v1625136714943!5m2!1svi!2s" },
    branch2: { name: "Hồ Hoàn Kiếm Clinic", phone: "(028) 123 123", address: "456 Downtown Ave, City, Country", map: "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3724.6963047525155!2d105.84168621476306!3d21.003465486011354!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135ac428c3a0e3b%3A0x8f6c0955e6a8c55e!2zSOG7kyBHxrDGoW0!5e0!3m2!1svi!2s!4v1625136760784!5m2!1svi!2s" },
    branch3: { name: "Hoàng Quốc Việt Clinic", phone: "(028) 678 826", address: "789 Suburb Rd, City, Country", map: "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3723.9265747536366!2d105.77621631476352!3d21.038127785994088!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3135ab32dd484c53%3A0x4201b89c8bdfd968!2zQ8O0bmcgdmnDqm4gQ-G6p3UgR2nhuqV5!5e0!3m2!1svi!2s!4v1625136805784!5m2!1svi!2s" },
  };

  return (
    <PageContainer>
      <NavBar />

      <HeroSection>
        <HeroContent>
          <Title style={{ color: 'white', fontSize: '4rem', marginBottom: '20px' }}>Sunflower Dentistry</Title>
          <Paragraph style={{ color: 'white', fontSize: '1.5rem', marginBottom: '30px' }}>
            Where Your Smile Blooms with Radiant Care
          </Paragraph>
          <Button type="primary" size="large" icon={<CalendarOutlined />} href="/booking" >
            Schedule Your Appointment
          </Button>
        </HeroContent>
      </HeroSection>

      <StyledContent>
        <Row gutter={[32, 32]} justify="center">
          <Col xs={24} md={6}>
            <FeatureCard>
              <SmileOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
              <Title level={3}>25k+</Title>
              <Paragraph>Happy Smiles</Paragraph>
            </FeatureCard>
          </Col>
          <Col xs={24} md={6}>
            <FeatureCard>
              <ProjectOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
              <Title level={3}>6+</Title>
              <Paragraph>Years of Excellence</Paragraph>
            </FeatureCard>
          </Col>
          <Col xs={24} md={6}>
            <FeatureCard>
              <FileDoneOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
              <Title level={3}>80+</Title>
              <Paragraph>Specialized Services</Paragraph>
            </FeatureCard>
          </Col>
          <Col xs={24} md={6}>
            <FeatureCard>
              <MailOutlined style={{ fontSize: '48px', color: '#1890ff' }} />
              <Title level={3}>1000+</Title>
              <Paragraph>Loyal Patients</Paragraph>
            </FeatureCard>
          </Col>
        </Row>

        <div style={{ margin: '80px 0' }}>
          <Title level={2} style={{ textAlign: 'center', marginBottom: '40px' }}>
            Our State-of-the-Art Services
          </Title>
          <StyledCarousel autoplay>
            <div>
              <img src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Service 1" />
            </div>
            <div>
              <img src="https://images.unsplash.com/photo-1606811841689-23dfddce3e95?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Service 2" />
            </div>
            <div>
              <img src="https://images.unsplash.com/photo-1629909615184-74f495363b67?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Service 3" />
            </div>
          </StyledCarousel>
        </div>

        <div style={{ margin: '80px 0' }}>
          <Title level={2} style={{ textAlign: 'center', marginBottom: '40px' }}>
            Meet Our Expert Dentists
          </Title>
          <Row gutter={[32, 32]} justify="center">
            <Col xs={24} sm={12} md={6}>
              <DentistCard
                cover={<img alt="Dentist" src="https://images.unsplash.com/photo-1537368910025-700350fe46c7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" />}
              >
                <Card.Meta title="Dr. Vo Ngoc Bao Thu" description="Wisdom Teeth Specialist" />
              </DentistCard>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <DentistCard
                cover={<img alt="Dentist" src="https://images.unsplash.com/photo-1622253692010-333f2da6031d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" />}
              >
                <Card.Meta title="Dr. Nguyen Huu Phuc" description="Cavity Treatment Specialist" />
              </DentistCard>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <DentistCard
                cover={<img alt="Dentist" src="https://images.unsplash.com/photo-1594824476967-48c8b964273f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" />}
              >
                <Card.Meta title="Dr. Ariana Grande" description="Teeth Whitening Specialist" />
              </DentistCard>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <DentistCard
                cover={<img alt="Dentist" src="https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" />}
              >
                <Card.Meta title="Dr. Mono Hoang" description="Tooth Replacement Specialist" />
              </DentistCard>
            </Col>
          </Row>
        </div>

        <MapSection>
          <Title level={2} style={{ textAlign: 'center', marginBottom: '40px' }}>
            Find Us Near You
          </Title>
          <Row gutter={[32, 32]}>
            <Col xs={24} md={12}>
              <iframe
                src={branches[selectedBranch].map}
                width="100%"
                height="450"
                style={{ border: 0 }}
                allowFullScreen=""
                loading="lazy"
                title="branch-map"
              ></iframe>
            </Col>
            <Col xs={24} md={12}>
              <Title level={3}>Select a Branch</Title>
              <Select defaultValue="branch1" style={{ width: '100%', marginBottom: '20px' }} onChange={handleBranchChange}>
                <Option value="branch1">Tôn Thất Thuyết Clinic</Option>
                <Option value="branch2">Hồ Hoàn Kiếm Clinic</Option>
                <Option value="branch3">Hoàng Quốc Việt Clinic</Option>
              </Select>
              <Card>
                <Title level={4}>{branches[selectedBranch].name}</Title>
                <Paragraph><EnvironmentOutlined /> {branches[selectedBranch].address}</Paragraph>
                <Paragraph><PhoneOutlined /> {branches[selectedBranch].phone}</Paragraph>
                <Paragraph><ClockCircleOutlined /> Mon-Sun: 9:00 AM - 6:00 PM</Paragraph>
                <Button type="primary" icon={<CalendarOutlined />} href="/booking">
                  Book Appointment
                </Button>
              </Card>
            </Col>
          </Row>
        </MapSection>
      </StyledContent>

      <StyledFooter>
  <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '0 20px' }}>
    <Row gutter={[32, 32]} justify="center">
      <Col xs={24} md={8}>
        <Title level={3} style={{ color: 'white' }}>About Us</Title>
        <Paragraph style={{ color: 'white' }}>
          Sunflower Dentistry is dedicated to providing top-notch dental care to our community with a focus on patient comfort and cutting-edge technology.
        </Paragraph>
      </Col>
      <Col xs={24} md={8}>
        <Title level={3} style={{ color: 'white' }}>Contact</Title>
        <Paragraph style={{ color: 'white' }}>
          VNUHCM Student Cultural House<br />
          Luu Huu Phuoc, Dong Hoa, Di An, Binh Duong<br />
          Email: dentistrysunflower@gmail.com<br />
          Phone: (123) 456-7890
        </Paragraph>
      </Col>
      <Col xs={24} md={8}>
        <Title level={3} style={{ color: 'white' }}>Opening Hours</Title>
        <Paragraph style={{ color: 'white' }}>
          Monday - Friday: 9:00 AM - 6:00 PM<br />
          Saturday: 9:00 AM - 2:00 PM<br />
          Sunday: Closed
        </Paragraph>
      </Col>
    </Row>
    <Row justify="center" style={{ marginTop: '40px' }}>
      <Col>
        <Paragraph style={{ color: 'white' }}>
          © 2024 Sunflower Dentistry. All rights reserved.
        </Paragraph>
      </Col>
    </Row>
  </div>
</StyledFooter>   
    </PageContainer>
  );
};

export default Homepage;