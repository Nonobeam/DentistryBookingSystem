import React from "react";
import NavBar from "./Nav";
import "antd/dist/reset.css"

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

const { Title, Paragraph } = Typography;

const Homepage = () => {
  return (
    <Layout className="layout">
      
      <NavBar />

      <Layout.Content style={{ padding: "0 50px" }}>
        <div className="hero-section" style={{ padding: "50px 0" }}>
          <Row gutter={[16, 16]} align="middle">
            <Col xs={24} md={12}>
              <Title>Top Vietnam Dentistry</Title>
              <Paragraph>
              Welcome to Sunflower Dentistry, where your smile matters most. 
              Our expert team provides personalized dental care in a friendly environment.              
              </Paragraph>
              <Button type="primary" style={{ marginRight: "10px" }}>
                Schedule Your Appointment
              </Button>
              <Button>Need Advise?</Button>
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
        <div
          className="features-section"
          style={{ padding: "50px 0", background: "#f0f2f5" }}
        >
          <Title level={2} style={{ textAlign: "center" }}>
            Things make Sunflower Dentistry different
          </Title>
          <Paragraph
            style={{ textAlign: "center", maxWidth: "600px", margin: "0 auto" }}
          >
            At Sunflower Dentistry, we stand out with our patient-centered approach, 
            state-of-the-art technology, and a warm, welcoming atmosphere. 
            Our experienced team is dedicated to personalized care, 
            ensuring every visit is comfortable and tailored to your needs.
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
                <ProjectOutlined
                  style={{ fontSize: "34px", color: "#1890ff" }}
                />
                <Title level={3}>6+</Title>
                <Paragraph>Years Experieces</Paragraph>
              </Card>
            </Col>
            
            <Col xs={24} sm={12} md={6}>
              <Card>
                <FileDoneOutlined
                  style={{ fontSize: "34px", color: "#1890ff" }}
                />
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
              {/* <img
                src="https://via.placeholder.com/400"
                alt="Map"
                style={{ width: "100%" }}
              /> */}
              <div>
                <iframe width="100%" height="400px" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com/maps?width=600&amp;height=600&amp;hl=en&amp;q=L%C6%B0u%20H%E1%BB%AFu%20Ph%C6%B0%E1%BB%9Bc,%20%C4%90%C3%B4ng%20Ho%C3%A0,%20D%C4%A9%20An,%20B%C3%ACnh%20D%C6%B0%C6%A1ng+(Sunflower%20Dentistry)&amp;t=&amp;z=15&amp;ie=UTF8&amp;iwloc=B&amp;output=embed"><a href="https://www.gps.ie/">gps systems</a></iframe></div>
            </Col>
            <Col xs={24} md={12}>
              <Title level={2}>Dentistry Branches</Title>
              <Paragraph>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Phasellus non dui ac felis tincidunt aliquam.
              </Paragraph>
            </Col>
          </Row>
        </div>
        <div
          className="video-section"
          style={{ padding: "50px 0", background: "#f0f2f5" }}
        >
          <Title level={2} style={{ textAlign: "center" }}>
            Why do you need a dentist?
          </Title>
          <Paragraph
            style={{ textAlign: "center", maxWidth: "600px", margin: "0 auto" }}
          >
            Regular dental visits are essential for maintaining oral health, 
            preventing tooth decay, and catching issues early. 
            A dentist ensures your smile stays healthy and bright through 
            professional cleanings and expert care.
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
            Experienced Dentists
          </Title>
          <Row gutter={[16, 16]} justify="center" style={{ marginTop: "30px" }}>
            <Col xs={24} sm={12} md={6}>
              <Card
                cover={
                  <img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />
                }
              >
                <Card.Meta
                  title="Vo Ngoc Bao Thu"
                  description="Wisdom Teeth"
                />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                cover={
                  <img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />
                }
              >
                <Card.Meta
                  title="Nguyen Huu Phuc"
                  description="Cravity Teeth"
                />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                cover={
                  <img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />
                }
              >
                <Card.Meta 
                  title="Ariana Grande" 
                  description="Whitening Teeth" />
              </Card>
            </Col>
            <Col xs={24} sm={12} md={6}>
              <Card
                cover={
                  <img alt="Dentist" src="https://ukiahdental.com/wp-content/uploads/2023/01/ukiah-dental-dentist-profile-image.jpg" />
                }
              >
                <Card.Meta
                  title="Mono Hoang"
                  description="Replace Teeth"
                />
              </Card>
            </Col>
          </Row>
        </div>
      </Layout.Content>
      <Layout.Footer style={{ textAlign: "center", backgroundColor: "cornflowerblue"}}>
        <Row gutter={[16, 16]}>
          <Col xs={24} sm={12} md={6}>
            <Title level={4}>Column One</Title>
            <Paragraph>Twenty One</Paragraph>
            <Paragraph>Thirty Two</Paragraph>
            <Paragraph>Fourty Three</Paragraph>
            <Paragraph>Fifty Four</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4}>Column Two</Title>
            <Paragraph>Sixty Five</Paragraph>
            <Paragraph>Seventy Six</Paragraph>
            <Paragraph>Eighty Seven</Paragraph>
            <Paragraph>Ninety Eight</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4}>Column Three</Title>
            <Paragraph>One Two</Paragraph>
            <Paragraph>Three Four</Paragraph>
            <Paragraph>Five Six</Paragraph>
            <Paragraph>Seven Eight</Paragraph>
          </Col>
          <Col xs={24} sm={12} md={6}>
            <Title level={4}>Follow Us</Title>
            <Paragraph>
              <FacebookOutlined /> <TwitterOutlined /> <YoutubeOutlined />{" "}
              <LinkedinOutlined />
            </Paragraph>
          </Col>
        </Row>
      </Layout.Footer>
    </Layout>
  );
};

export default Homepage;
