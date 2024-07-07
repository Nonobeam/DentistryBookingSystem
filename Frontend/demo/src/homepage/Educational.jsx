import React from 'react';
import styled from 'styled-components';
import NavBar from './Nav';

// Styled components for the page layout
const PageContainer = styled.div`
  font-family: Arial, sans-serif;
  max-width: 800px;
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

// Function to shuffle array randomly
function shuffleArray(array) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

// Educational component
const Educational = () => {
  // Dummy article links array
  const articleLinks = shuffleArray([
    {
      title: 'Effective Methods for Maintaining Dental Hygiene',
      description: 'Discover effective methods and tools for maintaining dental hygiene at home, crucial for preventing dental problems.',
      url: 'https://www.vinmec.com/vi/news/health-news/general-health-check/a-guide-to-taking-care-of-your-oral-health-properly/?link_type=related_posts',
      imageUrl: 'https://images.ctfassets.net/szez98lehkfm/187wtgWuunnxRdcPNZIwF9/79b720027c4cfd9831c914ba25dfedf8/MyIC_Article_114454?w=730&h=410&fm=jpg&fit=fill'
    },
    {
      title: 'The Impact of Poor Eating Habits on Oral Health',
      description: 'Explore how unhealthy eating habits contribute to dental issues and learn tips for a balanced diet that supports dental health.',
      url: 'https://www.nhs.uk/live-well/healthy-teeth-and-gums/take-care-of-your-teeth-and-gums/',
      imageUrl: 'https://www.forbes.com/advisor/wp-content/uploads/2023/10/image1-34-900x510.jpg'
    },
    {
      title: 'Pediatric Dental Care: Essential Tips for Parents',
      description: 'Learn essential tips for parents to care for childrens dental health, covering brushing techniques and choosing the right dental products.',
      url: 'https://www.healthline.com/health/dental-and-oral-health/best-practices-for-healthy-teeth',
      imageUrl: 'https://ihm.edu.au/wp-content/uploads/2022/09/MicrosoftTeams-image-23-scaled.jpg'
    },
    {
      title: 'Daily Habits to Preserve Your Dental Health',
      description: 'Discover daily habits like brushing, flossing, and regular dental check-ups that help maintain optimal dental health. Learn about daily habits that play a crucial role in preserving dental health. From proper brushing and flossing techniques to the importance of regular dental check-ups, this article provides actionable tips for maintaining optimal oral hygiene. Discover how consistent habits can prevent common dental problems and support long-term dental wellness.',
      url: 'https://www.betterhealth.vic.gov.au/health/conditionsandtreatments/teeth-care',
      imageUrl: 'https://www.macombsmiles.com/wp-content/uploads/2019/01/dental-anxiety-tips.jpg'
    },
    {
      title: 'Understanding Dental Restoration: What you need to know',
      description: 'Learn about dental restoration options such as crowns, bridges, and implants, and their benefits in restoring oral function.Gain insights into various dental restoration options, including crowns, bridges, and implants. Learn about the benefits of each treatment in restoring oral function and enhancing dental aesthetics. This article aims to educate readers on different dental restoration procedures, helping them make informed decisions about their oral health care.',
      url: 'https://www.dentalhealth.org/caring-for-my-teeth',
      imageUrl: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiW9JIAPuRSMUvSqjRCMGQ-uZ05SIUKZvhVA&s'
    },
    {
      title: 'Preventing Dental Problems Through Diet',
      description: 'Explore how dietary choices impact dental health and discover foods that promote strong teeth and gums while preventing common dental issues.Gain insights into various dental restoration options, including crowns, bridges, and implants. Learn about the benefits of each treatment in restoring oral function and enhancing dental aesthetics. This article aims to educate readers on different dental restoration procedures, helping them make informed decisions about their oral health care.',
      url: 'https://kidshealth.org/en/teens/teeth.html',
      imageUrl: 'https://www.verywellmind.com/thmb/ftfpT2r3LRgrT9XwNuEwHGczq7Q=/2121x0/filters:no_upscale():max_bytes(150000):strip_icc()/GettyImages-1402885491-89ea6d77cafb4e62a091b093de1354ad.jpg'
    },
    {
      title: 'The Importance of Proper Brushing Technique and Timing',
      description: 'Understand why proper brushing technique and timing are crucial for maintaining good oral hygiene and preventing dental problems.Learn why brushing your teeth correctly and at the right times of day is crucial for maintaining good oral hygiene. Understand how proper brushing techniques contribute to plaque removal and prevention of dental problems. This article emphasizes the role of brushing habits in achieving a healthy smile.',
      url: 'https://my.clevelandclinic.org/health/treatments/16914-oral-hygiene',
      imageUrl: 'https://skylinedentalassociates.com/wp-content/uploads/2022/12/shutterstock_1091950463.jpg'
    },
    {
      title: 'Common Dental Issues and Prevention Strategies',
      description: 'Explore common dental problems like cavities and gum disease, and learn effective prevention strategies for optimal oral health.Understand the detrimental effects of smoking on dental health, including increased risks of gum disease and oral cancer. This article provides insights into how smoking affects oral tissues and overall oral hygiene. Discover tips for quitting smoking to improve oral health and overall well-being.',
      url: 'https://www.webmd.com/oral-health/ss/slideshow-tooth-problems',
      imageUrl: 'https://wpcdn.us-east-1.vip.tn-cloud.net/www.pittsburghmagazine.com/content/uploads/2023/07/f/u/thumbnail-top-dentists-2023-1000x667-1.jpg'
    },
    {
      title: 'The Impact of Smoking on Oral Health',
      description: 'Learn about the effects of smoking on dental health, including risks like gum disease and oral cancer, and tips for quitting. Identify common dental problems such as cavities and gum disease in this informative article. Explore effective prevention strategies that help maintain optimal oral health and reduce the risk of dental issues. By understanding these common problems and their prevention methods, readers can take proactive steps towards better dental care.',
      url: 'https://dentistry.uic.edu/news-stories/the-best-foods-for-a-healthy-smile-and-whole-body/',
      imageUrl: 'https://kffhealthnews.org/wp-content/uploads/sites/2/2021/05/GettyImages-1284524335.jpeg'
    },
    {
      title: 'Signs to Watch Out For in Dental Health',
      description: 'Recognize signs and symptoms indicating potential dental health problems and understand the importance of regular dental check-ups.Recognize signs and symptoms that indicate potential dental health problems. Learn about the importance of regular dental check-ups for early detection and treatment of oral issues. This article empowers readers to take proactive measures in monitoring their dental health and seeking timely professional care when needed.',
      url: 'https://www.nidirect.gov.uk/conditions/dental-problems',
      imageUrl: 'https://websterdds.com/wp-content/uploads/2021/05/dental-specialties.jpg'
    },
  ]);

  return (
    <>
      <NavBar />
      <PageContainer>
        <PageTitle>Educational Page</PageTitle>
        <PageParagraph>
          Welcome to our Educational Page dedicated to dental care! Here, you can explore a curated collection of articles that cover various aspects of maintaining good oral hygiene and preventing dental problems. Each article provides valuable insights and practical tips to help you take better care of your teeth and gums. Click on the links below to read more:
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
