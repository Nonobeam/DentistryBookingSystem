import React from 'react';
import styled from 'styled-components';
import { Layout, Typography, Card, Button, Row, Col } from 'antd';
import NavBar from './Nav';

const { Title, Paragraph } = Typography;
const { Content } = Layout;

const PageContainer = styled(Layout)`
  font-family: 'Poppins', sans-serif;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
`;

const HeroSection = styled.div`
  background: url('https://images.unsplash.com/photo-1606811841689-23dfddce3e95?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80') center/cover no-repeat;
  height: 70vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
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
  position: relative;
  z-index: 1;
`;

const StyledContent = styled(Content)`
  padding: 50px 50px;
  @media (max-width: 768px) {
    padding: 30px 20px;
  }
`;

const ServiceCard = styled(Card)`
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  }

  .ant-card-cover img {
    height: 200px;
  object-fit: cover;
  }

  .ant-card-meta-title {
    font-size: 1.2em;
    font-weight: 600;
  }
`;

const StyledFooter = styled(Layout.Footer)`
  background-color: #1976d2;
  color: white;
  padding: 20px 0;
`;


const Services = () => {
  const services = [
    {
      name: 'Teeth Cleaning',
      description: 'Professional cleaning to maintain oral hygiene and prevent dental issues.',
      imgSrc: 'https://i0.wp.com/glenburniedentalgroup.com/wp-content/uploads/2022/08/Cleaning-teeth.webp?fit=847%2C460&ssl=1',
    },
    {
      name: 'Fillings and Restorations',
      description: 'Repair damaged teeth and restore their function and appearance.',
      imgSrc: 'https://d3b3by4navws1f.cloudfront.net/iStock_000034747766_Comparison_2.png',
    },
    {
      name: 'Root Canal Therapy',
      description: 'Save severely damaged or infected teeth with advanced endodontic treatment.',
      imgSrc: 'https://cdn-5ecc40d4c1ac18016c0585b8.closte.com/wp-content/uploads/2021/04/root-canal-procedure-step-by-step-1024x575.png',
    },
    {
      name: 'Orthodontics',
      description: 'Achieve a straighter smile with braces or clear aligners.',
      imgSrc: 'https://www.clairechodds.com/wp-content/uploads/2021/08/clear-braces-mission-viejo.jpg',
    },
    {
      name: 'Dental Implants',
      description: 'Replace missing teeth with permanent, natural-looking implants.',
      imgSrc: 'https://nhakhoanhantam.com/stmresource/files/trong-rang-implant/cam-implant-chi-dinh-va-chong-chi-dinh.jpg',
    },
    {
      name: 'Teeth Whitening',
      description: 'Brighten your smile with professional whitening treatments.',
      imgSrc: 'https://ichef.bbci.co.uk/news/1024/branded_news/081A/production/_118647020_gettyimages-475063916.jpg',
    },
  ];

  return (
    <PageContainer>
      <NavBar />
      <HeroSection>
        <HeroContent>
          <Title style={{ color: 'white', fontSize: '3.5rem', marginBottom: '20px' }}>
            Exceptional Dental Care
          </Title>
          <Paragraph style={{ color: 'white', fontSize: '1.2rem', marginBottom: '30px' }}>
            Your journey to a healthier, brighter smile starts here
          </Paragraph>
          <Button type="primary" size="large">
            Book Appointment
          </Button>
        </HeroContent>
      </HeroSection>
      <StyledContent>
        <Title level={2} style={{ textAlign: 'center', marginBottom: '50px' }}>
          Our Comprehensive Dental Services
        </Title>
        <Row gutter={[32, 32]}>
          {services.map((service) => (
            <Col xs={24} sm={12} md={8} key={service.name}>
              <ServiceCard
                hoverable
                cover={<img alt={service.name} src={service.imgSrc} />}
              >
                <Card.Meta
                  title={service.name}
                  description={service.description}
                />
              </ServiceCard>
            </Col>
          ))}
        </Row>
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

export default Services;