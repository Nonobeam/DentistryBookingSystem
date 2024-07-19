import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Layout, Typography, Card, Button, Row, Col, Carousel, Divider } from "antd";
import {
  FacebookOutlined,
  TwitterOutlined,
  InstagramOutlined,
  LinkedinOutlined,
  ArrowUpOutlined,
  ClockCircleOutlined,
  PhoneOutlined,
  MailOutlined,
  EnvironmentOutlined
} from "@ant-design/icons";
import NavBar from './Nav';

const { Title, Paragraph } = Typography;
const { Content, Footer } = Layout;

const PageContainer = styled(Layout)`
  font-family: 'Poppins', sans-serif;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
`;

const HeroSection = styled.div`
  background: url('https://images.unsplash.com/photo-1606811971618-4486d14f3f99?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80') center/cover no-repeat;
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

const ArticleCard = styled(Card)`
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

const StyledCarousel = styled(Carousel)`
  margin-bottom: 50px;

  .slick-slide {
    height: 400px;
    overflow: hidden;
  }

  .slick-slide img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
`;

const BackToTopButton = styled(Button)`
  position: fixed;
  bottom: 20px;
  right: 20px;
`;

const StyledFooter = styled(Layout.Footer)`
  background-color: #1976d2;
  color: white;
  padding: 20px 0;
`;

const SocialIcon = styled.a`
  color: white;
  font-size: 24px;
  margin-right: 20px;
  transition: color 0.3s ease;

  &:hover {
    color: #1890ff;
  }
`;

const ContactItem = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 10px;

  .anticon {
    margin-right: 10px;
  }
`;

const Educational = () => {
  const [showBackToTop, setShowBackToTop] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setShowBackToTop(window.scrollY > 300);
    };

    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const articleLinks = [
    {
      title: 'Effective Methods for Maintaining Dental Hygiene',
      description: 'Discover effective methods and tools for maintaining dental hygiene at home.',
      url: 'https://www.vinmec.com/vi/news/health-news/general-health-check/a-guide-to-taking-care-of-your-oral-health-properly/?link_type=related_posts',
      imageUrl: 'https://images.ctfassets.net/szez98lehkfm/187wtgWuunnxRdcPNZIwF9/79b720027c4cfd9831c914ba25dfedf8/MyIC_Article_114454?w=730&h=410&fm=jpg&fit=fill'
    },
    {
      title: 'The Impact of Poor Eating Habits on Oral Health',
      description: 'Explore how unhealthy eating habits contribute to dental issues.',
      url: 'https://www.nhs.uk/live-well/healthy-teeth-and-gums/take-care-of-your-teeth-and-gums/',
      imageUrl: 'https://www.forbes.com/advisor/wp-content/uploads/2023/10/image1-34-900x510.jpg'
    },
    {
      title: 'Pediatric Dental Care: Essential Tips for Parents',
      description: 'Learn essential tips for parents to care for children’s dental health.',
      url: 'https://www.healthline.com/health/dental-and-oral-health/best-practices-for-healthy-teeth',
      imageUrl: 'https://ihm.edu.au/wp-content/uploads/2022/09/MicrosoftTeams-image-23-scaled.jpg'
    },
    {
      title: 'Daily Habits to Preserve Your Dental Health',
      description: 'Discover daily habits that help maintain optimal dental health.',
      url: 'https://www.betterhealth.vic.gov.au/health/conditionsandtreatments/teeth-care',
      imageUrl: 'https://www.macombsmiles.com/wp-content/uploads/2019/01/dental-anxiety-tips.jpg'
    },
    {
      title: 'Understanding Dental Restoration: What You Need to Know',
      description: 'Learn about dental restoration options such as crowns, bridges, and implants.',
      url: 'https://www.dentalhealth.org/caring-for-my-teeth',
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiW9JIAPuRSMUvSqjRCMGQ-uZ05SIUKZvhVA&s'
    },
    {
      title: 'Preventing Dental Problems Through Diet',
      description: 'Explore how dietary choices impact dental health.',
      url: 'https://kidshealth.org/en/teens/teeth.html',
      imageUrl: 'https://www.verywellmind.com/thmb/ftfpT2r3LRgrT9XwNuEwHGczq7Q=/2121x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1402885491-89ea6d77cafb4e62a091b093de1354ad.jpg'
    },
    {
      title: 'Proper Brushing Technique and Timing',
      description: 'Understand why proper brushing technique and timing are crucial.',
      url: 'https://my.clevelandclinic.org/health/treatments/16914-oral-hygiene',
      imageUrl: 'https://skylinedentalassociates.com/wp-content/uploads/2022/12/shutterstock_1091950463.jpg'
    },
    {
      title: 'Common Dental Issues and Prevention Strategies',
      description: 'Explore common dental problems and learn effective prevention strategies.',
      url: 'https://www.webmd.com/oral-health/ss/slideshow-tooth-problems',
      imageUrl: 'https://wpcdn.us-east-1.vip.tn-cloud.net/www.pittsburghmagazine.com/content/uploads/2023/07/f/u/thumbnail-top-dentists-2023-1000x667-1.jpg'
    },
    {
      title: 'The Impact of Smoking on Oral Health',
      description: 'Learn about the effects of smoking on dental health.',
      url: 'https://dentistry.uic.edu/news-stories/the-best-foods-for-a-healthy-smile-and-whole-body/',
      imageUrl: 'https://kffhealthnews.org/wp-content/uploads/sites/2/2021/05/GettyImages-1284524335.jpeg'
    },
    {
      title: 'Signs to Watch Out For in Dental Health',
      description: 'Recognize signs indicating potential dental health problems.',
      url: 'https://www.nidirect.gov.uk/conditions/dental-problems',
      imageUrl: 'https://websterdds.com/wp-content/uploads/2021/05/dental-specialties.jpg'
    },
  ];

  return (
    <PageContainer>
      <NavBar />
        <HeroSection>
        <HeroContent>
          <Title style={{ color: 'white', fontSize: '3.5rem', marginBottom: '20px' }}>
            Dental Education Hub
          </Title>
          <Paragraph style={{ color: 'white', fontSize: '1.2rem', marginBottom: '30px' }}>
            Empowering you with knowledge for better oral health
          </Paragraph>
          <Button type="primary" size="large">
            Explore Articles
          </Button>
        </HeroContent>
        </HeroSection>

      <StyledContent>
        <Title level={2} style={{ textAlign: 'center', marginBottom: '50px' }}>
          Featured Articles
        </Title>

        <StyledCarousel autoplay>
          <div><img src="https://images.unsplash.com/photo-1606811971618-4486d14f3f99?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Dental Care 1" /></div>
          <div><img src="https://images.unsplash.com/photo-1588776814546-1ffcf47267a5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Dental Care 2" /></div>
          <div><img src="https://images.unsplash.com/photo-1445527815219-ecbfec67492e?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80" alt="Dental Care 3" /></div>
        </StyledCarousel>

        <Row gutter={[32, 32]}>
          {articleLinks.map((article, index) => (
            <Col xs={24} sm={12} md={8} key={index}>
              <ArticleCard
                hoverable
                cover={<img alt={article.title} src={article.imageUrl} />}
              >
                <Card.Meta
                  title={article.title}
                  description={article.description}
                />
                <Button type="primary" href={article.url} target="_blank" rel="noopener noreferrer" style={{ marginTop: '16px' }}>
                  Read More
                </Button>
              </ArticleCard>
            </Col>
          ))}
        </Row>

        {showBackToTop && (
          <BackToTopButton
            type="primary"
            shape="circle"
            icon={<ArrowUpOutlined />}
            size="large"
            onClick={scrollToTop}
          />
        )}
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

export default Educational;