import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Form, Input, Button, Typography, Alert } from 'antd';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';

const { Title } = Typography;

const API_URL = 'http://localhost:8080/api/v1/auth/authenticate';

const LoginContainer = styled.div`
  display: flex;
  height: 100vh;
  width: 100vw;
  background-color: #f0f2f5;
`;

const LoginBox = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
`;

const LoginForm = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 60px;
  background-color: white;
`;

const FormWrapper = styled.div`
  width: 100%;
  max-width: 600px;  // Increased to accommodate wider inputs
`;

const ImageContainer = styled.div`
  flex: 1;
  background-image: url(${props => props.image});
  background-size: cover;
  background-position: center;
  transition: background-image 0.5s ease-in-out;
`;

const StyledForm = styled(Form)`
  .ant-form-item {
    margin-bottom: 20px;
  }

  .ant-input,
  .ant-input-password {
    width: 500px;  // Increased to 500px (300px + 200px)
  }

  .ant-btn {
    width: 500px;  // Make button match input width
  }
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
  width: 500px;  // Match width of inputs
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
  const [currentImage, setCurrentImage] = useState(0);
  const navigate = useNavigate();
  const location = useLocation();

  const images = [
    'https://radonvietnam.vn/wp-content/uploads/2022/08/setup-tu-van-phong-kham-nha-khoa-2.jpg',
    'https://www.docosan.com/blog/wp-content/uploads/2022/01/phong-kham-nha-khoa-uy-tin-6.jpg',
    'https://nhakhoakim.com/wp-content/uploads/2018/07/nha-khoa-dong-da.jpg',
    'https://www.lekimdung.com/wp-content/uploads/2021/08/bac-si-nha-khoa-1.jpg',
    'https://nhakhoakim.com/wp-content/uploads/2018/12/Kh%C3%A1ch-h%C3%A0ng-t%E1%BA%A9y-tr%E1%BA%AFng-r%C4%83ng-t%E1%BA%A1i-Nha-khoa-Kim.jpg',
  ];

  useEffect(() => {
    if (location.state && location.state.message) {
      setSuccessMessage(location.state.message);
    }
    if (location.state && location.state.errormessage) {
      setErrorMessage(location.state.errormessage);
    }

    const interval = setInterval(() => {
      setCurrentImage((prevImage) => (prevImage + 1) % images.length);
    }, 3000);

    return () => clearInterval(interval);
  }, [location.state, images.length]);

  const onFinish = async (values) => {
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

      setErrorMessage('');
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
        <LoginForm>
          <FormWrapper>
            <Title level={2} style={{ color: '#1890ff', textAlign: 'center', marginBottom: '30px' }}>LOGIN</Title>
            <StyledForm
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
            </StyledForm>
            <ExtraLinks>
              <a href='/forgot'>Forgot Password?</a> | 
              <a href='/signup'>Sign Up</a> | 
              <a href='/'>Home</a>
            </ExtraLinks>
          </FormWrapper>
        </LoginForm>
        <ImageContainer image={images[currentImage]} />
      </LoginBox>
    </LoginContainer>
  );
};

export default Login;