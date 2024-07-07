import React from 'react';
import NavBar from './Nav';

const Services = () => {
  const styles = {
    page: {
      padding: '40px',
      backgroundColor: '#f9f9f9',
      textAlign: 'center',
      fontFamily: 'Arial, Helvetica, sans-serif', // Thay đổi font chữ
    },
    title: {
      color: '#2a9d8f',
      fontSize: '3em',
      marginBottom: '20px',
      textTransform: 'uppercase',
      letterSpacing: '2px',
      fontWeight: 'bold',
      fontFamily: 'Arial, Helvetica, sans-serif', // Thay đổi font chữ
    },
    listContainer: {
      display: 'flex',
      justifyContent: 'center',
      gap: '20px',
      flexWrap: 'wrap',
      marginTop: '20px',
    },
    list: {
      listStyleType: 'none',
      padding: 0,
      fontSize: '1.2em',
    },
    listItem: {
      margin: '10px',
      backgroundColor: '#ffffff',
      padding: '20px',
      borderRadius: '10px',
      boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
      transition: 'transform 0.2s, box-shadow 0.2s',
      width: '250px',
      textAlign: 'center',
      fontFamily: 'Arial, Helvetica, sans-serif', // Thay đổi font chữ
    },
    listItemHover: {
      transform: 'scale(1.05)',
      boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)',
    },
    image: {
      width: '100%',
      height: '150px', // Đặt kích thước hình ảnh cố định
      objectFit: 'cover', // Đảm bảo các hình ảnh không bị méo khi thay đổi kích thước
      borderRadius: '10px 10px 0 0',
    },
    serviceName: {
      marginTop: '10px',
      fontWeight: 'bold',
      fontSize: '1.2em',
      fontFamily: 'Arial, Helvetica, sans-serif', // Thay đổi font chữ
    },
  };

  const servicesTop = [
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
  ];

  const servicesBottom = [
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

  return (
    <>
      <NavBar />
      <div style={styles.page}>
        <h1 style={styles.title}>Some Dental Services In Our System</h1>
        <div style={styles.listContainer}>
          <ul style={styles.list}>
            {servicesTop.map((service) => (
              <li
                key={service.name}
                style={styles.listItem}
                onMouseEnter={(e) => {
                  e.currentTarget.style.transform = 'scale(1.05)';
                  e.currentTarget.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.2)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.transform = 'scale(1)';
                  e.currentTarget.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
                }}
              >
                <img src={service.imgSrc} alt={service.name} style={styles.image} />
                <div style={styles.serviceName}>{service.name}</div>
              </li>
            ))}
          </ul>
          <ul style={styles.list}>
            {servicesBottom.map((service) => (
              <li
                key={service.name}
                style={styles.listItem}
                onMouseEnter={(e) => {
                  e.currentTarget.style.transform = 'scale(1.05)';
                  e.currentTarget.style.boxShadow = '0 8px 16px rgba(0, 0, 0, 0.2)';
                }}
                onMouseLeave={(e) => {
                  e.currentTarget.style.transform = 'scale(1)';
                  e.currentTarget.style.boxShadow = '0 4px 8px rgba(0, 0, 0, 0.1)';
                }}
              >
                <img src={service.imgSrc} alt={service.name} style={styles.image} />
                <div style={styles.serviceName}>{service.name}</div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </>
  );
};

export default Services;
