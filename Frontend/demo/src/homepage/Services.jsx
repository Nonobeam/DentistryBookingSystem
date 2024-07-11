import React from 'react';
import styled from 'styled-components';
import styled from 'styled-components';
import NavBar from './Nav';
import "antd/dist/reset.css"
import { Layout, Row, Col, Typography } from "antd";
import {
  FacebookOutlined,
  TwitterOutlined,
  YoutubeOutlined,
  LinkedinOutlined,
} from "@ant-design/icons";

const { Title, Paragraph } = Typography;

const PageContainer = styled.div`
  font-family: 'Roboto', sans-serif;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 40px 20px;
`;

const IntroSection = styled.div`
  background-color: #3498db;
  color: white;
  padding: 40px;
  border-radius: 15px;
  margin-bottom: 40px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
`;

const PageTitle = styled.h1`
  text-align: center;
  font-weight: bold;
  color: white;
  font-size: 3rem;
  text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
  margin-bottom: 20px;
`;

const ServiceList = styled.ul`
  display: flex;
  justify-content: center;
  gap: 30px;
  flex-wrap: wrap;
  list-style-type: none;
  padding: 0;
`;

const ServiceItem = styled.li`
  background-color: white;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  width: 280px;
  text-align: center;

  &:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  }
`;

const ServiceImage = styled.img`
  width: 100%;
  height: 180px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 15px;
`;

const ServiceName = styled.div`
  font-weight: bold;
  font-size: 1.2em;
  color: #2c3e50;
`;

const StyledFooter = styled(Layout.Footer)`
  text-align: center;
  background-color: #34495e;
  color: white;
  padding: 40px 0;
`;

const Services = () => {
  const services = [
    {
      name: 'Teeth Cleaning',
      imgSrc: 'https://i0.wp.com/glenburniedentalgroup.com/wp-content/uploads/2022/08/Cleaning-teeth.webp?fit=847%2C460&ssl=1',
    },
    {
      name: 'Fillings and Restorations',
      imgSrc: 'https://d3b3by4navws1f.cloudfront.net/iStock_000034747766_Comparison_2.png',
    },
    {
      name: 'Root Canal Therapy',
      imgSrc: 'https://cdn-5ecc40d4c1ac18016c0585b8.closte.com/wp-content/uploads/2021/04/root-canal-procedure-step-by-step-1024x575.png',
    },
    {
      name: 'Orthodontics (Braces)',
      imgSrc: 'https://www.clairechodds.com/wp-content/uploads/2021/08/clear-braces-mission-viejo.jpg',
    },
    {
      name: 'Dental Implants',
      imgSrc: 'https://nhakhoanhantam.com/stmresource/files/trong-rang-implant/cam-implant-chi-dinh-va-chong-chi-dinh.jpg',
    },
    {
      name: 'Teeth Whitening',
      imgSrc: 'https://ichef.bbci.co.uk/news/1024/branded_news/081A/production/_118647020_gettyimages-475063916.jpg',
    },
  ];

const Services = () => {
  return (
    <>
      <NavBar />
      <PageContainer>
        <IntroSection>
          <PageTitle>Our Popular Dental Services</PageTitle>
          <Paragraph style={{ color: 'white', fontSize: '1.2em', textAlign: 'center' }}>
            Discover our comprehensive range of dental services designed to keep your smile healthy and bright.
          </Paragraph>
        </IntroSection>
        <ServiceList>
          {services.map((service) => (
            <ServiceItem key={service.name}>
              <ServiceImage src={service.imgSrc} alt={service.name} />
              <ServiceName>{service.name}</ServiceName>
            </ServiceItem>
          ))}
        </ServiceList>
      </PageContainer>
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
    </>
  );
};

export default Services;