import React, { useState } from 'react';
import axios from 'axios';
import { Form, Input, Button, Typography, Alert } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { useEffect } from 'react';
import styled from 'styled-components';

const { Title } = Typography;

const API_URL = 'http://localhost:8080/api/v1/auth/authenticate';

const LoginContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
`;

const LoginBox = styled.div`
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
    background-color: #21867a;
  }
`;

const ExtraLinks = styled.div`
  margin-top: 20px;
  text-align: center;
  a {
    color: #1890ff;
    margin: 0 5px;
    &:hover {
      text-decoration: underline;
    }
  }
`;

const Login = () => {
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    if (location.state && location.state.message) {
      setSuccessMessage(location.state.message);
    }
    if (location.state && location.state.errormessage) {
      setErrorMessage(location.state.errormessage);
    }
  }, [location.state]);

  const onFinish = async (values) => {
    setErrorMessage('');
    setSuccessMessage('');
    setLoading(true);
    try {
      const response = await axios.post(
        API_URL,
        {
          mail: values.mail,
          password: values.password,
        },
        {
          headers: {
            'Content-Type': 'application/json',
          },
          withCredentials: true,
        }
      );

      const { access_token, refresh_token, role } = response.data;
      const expirationTime = new Date().getTime() + 45 * 60 * 1000; // 45 mins

      localStorage.setItem('token', access_token);
      localStorage.setItem('refreshToken', refresh_token);
      localStorage.setItem('role', role);
      localStorage.setItem('expirationTime', expirationTime);

      setLoading(false);

      if (role === 'CUSTOMER') navigate('/');
      else if (role === 'STAFF') navigate('/staff');
      else if (role === 'ADMIN') navigate('/admin');
      else if (role === 'DENTIST') navigate('/dentist');
      else if (role === 'MANAGER') navigate('/manager');
      else if (role === 'BOSS') navigate('/boss');
    } catch (error) {
      setLoading(false);
      if (error.response && error.response.status === 403) {
        setErrorMessage("Wrong email or password. Activate your account through email first if you haven't done so.");
      } else {
        setErrorMessage(error.response?.data?.message);
      }
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <LoginContainer>
      <LoginBox>
        <Title level={2} style={{ color: '#1890ff' }}>LOGIN</Title>
        <Form
          name='login'
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          {successMessage && (
            <Form.Item>
              <Alert message={successMessage} type='success' showIcon />
            </Form.Item>
          )}
          {errorMessage && (
            <Form.Item>
              <Alert message={errorMessage} type='error' showIcon />
            </Form.Item>
          )}
          <Form.Item
            name='mail'
            rules={[
              { required: true, message: 'Please input your email' },
            ]}
          >
            <Input placeholder='Email' />
          </Form.Item>

          <Form.Item
            name='password'
            rules={[
              { required: true, message: 'Please input your password!' },
            ]}
          >
            <Input.Password placeholder='Password' />
          </Form.Item>

          <Form.Item>
            <StyledButton type='primary' htmlType='submit' loading={loading}>
                  Log In
            </StyledButton>
          </Form.Item>
        </Form>
        <ExtraLinks>
          <a href='/forgot'>Forgot Password?</a> | 
          <a href='/signup'>Sign Up</a> | 
          <a href='/'>Home</a>
        </ExtraLinks>
      </LoginBox>
    </LoginContainer>
  );
};

export default Login;
