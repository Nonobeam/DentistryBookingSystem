import React from 'react';
import styled from 'styled-components';
import { Result, Button } from 'antd';
import { Link } from 'react-router-dom';

const StyledContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
`;

const NotAuthorized = () => {
  return (
    <StyledContainer>
      <Result
        status="403"
        title="403"
        subTitle="Sorry, this page doesn't exist or you are not authorized to view."
        extra={<Button type="primary"><Link to="/">Back to Home</Link></Button>}
      />
    </StyledContainer>
  );
};

export default NotAuthorized;
