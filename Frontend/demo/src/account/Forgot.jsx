import React, { useState } from 'react';
import axios from 'axios';
import { Form, Input, Button, Typography, Alert, Spin } from 'antd';
import styled from 'styled-components';

const { Title } = Typography;

const ForgotPasswordContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
`;

const ForgotPasswordBox = styled.div`
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  text-align: center;
`;

const StyledButton = styled(Button)`
  width: 100%;
  background-color: #1890ff;
  border: none;
  &:hover {
    background-color: #167acb;
  }
`;

const ExtraLink = styled.div`
  margin-top: 20px;
  text-align: center;
  a {
    color: #1890ff;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const ForgotPassword = () => {
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const onFinish = async (values) => {
    setLoading(true);
    console.log('Success:', values);
    try {
      const response = await axios.post(`http://localhost:8080/user/forgotPassword?mail=${values.email}`);
      setSuccessMessage("The reset password link has been sent to your email.");
      setErrorMessage(""); 
    } catch (error) {
      setErrorMessage("An error occurred. Please try again later.");
      setSuccessMessage(""); 
    }
    setLoading(false);
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <ForgotPasswordContainer>
      <ForgotPasswordBox>
        <Title level={2} style={{ color: '#1890ff' }}>Forgot your password?</Title>
        <p>We'll send you an email to help you reset your password.</p>

        {successMessage && (
          <Alert message={successMessage} type="success" showIcon style={{ marginBottom: '20px' }} />
        )}
        {errorMessage && (
          <Alert message={errorMessage} type="error" showIcon style={{ marginBottom: '20px' }} />
        )}

        <Form
          name="forgotPassword"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          <Form.Item
            name="email"
            rules={[
              { required: true, message: 'Please input your email address!' },
              { type: 'email', message: 'The input is not valid E-mail!' }
            ]}
          >
            <Input placeholder="Enter your email address" />
          </Form.Item>

          <Form.Item>
            <StyledButton type="primary" htmlType="submit">
              {loading ? <Spin /> : 'Send Reset Link'}
            </StyledButton>
          </Form.Item>
        </Form>
        <ExtraLink>
          <a href="/login">Return to Login</a>
        </ExtraLink>
      </ForgotPasswordBox>
    </ForgotPasswordContainer>
  );
};

export default ForgotPassword;
