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
  color: #333;
`;

const PageParagraph = styled.p`
  text-align: justify;
  line-height: 1.5;
  font-weight: 500;
  color: #555;
`;

const ArticleList = styled.ul`
  list-style-type: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
`;

const ArticleItem = styled.li`
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
`;

const ArticleImage = styled.img`
  width: 100%;
  height: auto;
  border-radius: 5px;
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: scale(1.05);
  }
`;

const ArticleLink = styled.a`
  display: block;
  margin-top: 10px;
  font-weight: bold;
  color: #007bff;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;

const ArticleDescription = styled.p`
  margin-top: 10px;
  font-size: 0.9em;
  color: #777;
`;

const Educational = () => {
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
    <>
      <NavBar />
      <PageContainer>
        <PageTitle>Educational Page</PageTitle>
        <PageParagraph>
          Welcome to our Educational Page! Here, you can explore a curated collection of articles that cover various aspects of maintaining good oral hygiene and preventing dental problems. Click on the links below to read more:
        </PageParagraph>
        <ArticleList>
          {articleLinks.map((article, index) => (
            <ArticleItem key={index}>
              <ArticleImage src={article.imageUrl} alt={`Article ${index + 1}`} />
              <ArticleLink href={article.url} target="_blank" rel="noopener noreferrer">
                {article.title}
              </ArticleLink>
              <ArticleDescription>{article.description}</ArticleDescription>
            </ArticleItem>
          ))}
        </ArticleList>
      </PageContainer>
    </>
  );
};

export default Educational;
