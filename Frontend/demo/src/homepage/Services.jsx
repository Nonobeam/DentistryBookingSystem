import React from 'react';
import styled from 'styled-components';
import NavBar from './Nav';

// Styled components for the page layout
const PageContainer = styled.div`
  font-family: Arial, sans-serif;
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
`;

const PageTitle = styled.h1`
  text-align: center;
  font-weight: bold;
  margin-bottom: 40px;
  letter-spacing: 2px;
`;

const ServiceList = styled.ul`
  list-style-type: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
`;

const ServiceItem = styled.li`
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out, box-shadow 0.2s;
  text-align: center;
  
  &:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  }
`;

const ServiceImage = styled.img`
  width: 260px;
  height: 180px;
  border-radius: 5px 5px 0 0;
`;

const ServiceName = styled.div`
  margin-top: 10px;
  font-weight: bold;
  font-size: 1.2em;
  color: #555;
`;

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
        <PageTitle>Our Dental Services</PageTitle>
        <ServiceList>
          {services.map((service) => (
            <ServiceItem key={service.name}>
              <ServiceImage src={service.imgSrc} alt={service.name} />
              <ServiceName>{service.name}</ServiceName>
            </ServiceItem>
          ))}
        </ServiceList>
      </PageContainer>
    </>
  );
};

export default Services;
