import React, { useState } from 'react';
import axios from 'axios';
import { Form, Input, Button, Typography, Alert } from 'antd';
import { Navigate, useNavigate } from 'react-router-dom';
import { useLocation } from 'react-router-dom';
import { useEffect } from 'react';

const { Title } = Typography;

const API_URL = 'http://localhost:8080/api/v1/auth/authenticate';

const Login = () => {
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
    console.log('Form values:', values);
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

      console.log('Token:', localStorage.getItem('token'));
      console.log('Role:', role);
      setErrorMessage(''); // Clear previous error message
      navigate('/'); // Navigate back to homepage
    } catch (error) {
      console.error('Failed to login:', error);
      if (error.response && error.response.status === 403) {
        setErrorMessage('Wrong email or password. Please try again.');
      } else {
        setErrorMessage('An error occurred. Please try again later.');
      }
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div style={{ display: 'flex', height: '100vh' }}>
      <div
        style={{
          flex: 1,
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <div style={{ padding: '30px' }}>
          <Title>Log In</Title>
          <Form
            name='login'
            initialValues={{ remember: true }}
            onFinish={onFinish}
            onFinishFailed={onFinishFailed}
            style={{ minWidth: '270px' }}>
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
                {
                  required: true,
                  message: 'Please input your email',
                },
              ]}>
              <Input placeholder='Email' />
            </Form.Item>

            <Form.Item
              name='password'
              rules={[
                { required: true, message: 'Please input your password!' },
              ]}>
              <Input.Password placeholder='Password' />
            </Form.Item>

            <Form.Item>
              <Button
                type='primary'
                htmlType='submit'
                style={{ width: '100%' }}>
                Log In
              </Button>
            </Form.Item>
            <a href='/forgot'>Forgot Password?</a>
            <br></br>

            <a href='/signup'>No account yet? Sign Up</a>
            <br></br>
            <a href='/'>Return home</a>
          </Form>
        </div>
      </div>

      <div
        style={{
          flex: 1,
          backgroundColor: '#f0f2f5',
          maxWidth: '50vw',
          maxHeight: '100vh',
        }}>
        <img
          src='https://www.dpinc.net/wp-content/uploads/2021/03/9-scaled.jpg'
          style={{ height: '100%' }}></img>
      </div>
    </div>
  );
};

export default Login;
