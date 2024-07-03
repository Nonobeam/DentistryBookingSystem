import React, { useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import { Form, Input, Button, Typography, Alert } from 'antd';

const { Title } = Typography;

const ResetPassword = () => {
  const { token } = useParams();
  const navigate = useNavigate();
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const onFinish = async (values) => {
    const { password } = values;
    try {
      await axios.post(`http://localhost:8080/user/resetPassword/${token}?password=${password}`);
      setSuccessMessage("Your password has been reset successfully. Redirecting to login page...");
      setErrorMessage("");

      setTimeout(() => {
        navigate('/login');
      }, 3000);
    } 
    catch (error) {
      setErrorMessage(error.response?.data || "An error occurred. Please try again later.");
      setSuccessMessage("");
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div style={{ display: 'flex', height: '100vh', justifyContent: 'center', alignItems: 'center' }}>
      <div style={{ maxWidth: '500px', width: '100%', padding: '45px', backgroundColor: "ghostwhite", borderRadius: '20px' }}>
        <Title>Reset your password</Title>
        <p>Please enter your new password below.</p>

        {successMessage && (
          <Alert message={successMessage} type="success" showIcon style={{ marginBottom: '20px' }} />
        )}
        {errorMessage && (
          <Alert message={errorMessage} type="error" showIcon style={{ marginBottom: '20px' }} />
        )}

        <Form
          name="resetPassword"
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          <Form.Item
            name="password"
            rules={[
              { required: true, message: 'Please input your new password!' },
              { min: 8, message: 'Password must be at least 8 characters long!' }
            ]}
            hasFeedback
          >
            <Input.Password placeholder="Enter your new password" />
          </Form.Item>

          <Form.Item
            name="confirmPassword"
            dependencies={['password']}
            hasFeedback
            rules={[
              { required: true, message: 'Please confirm your password!' },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue('password') === value) {
                    return Promise.resolve();
                  }
                  return Promise.reject(new Error('The two passwords that you entered do not match!'));
                },
              }),
            ]}
          >
            <Input.Password placeholder="Confirm your new password" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
              Reset Password
            </Button>
          </Form.Item>
          <Form.Item>
            <Button type="default" style={{ width: '100%' }} href='/forgot'>
              Send reset link again?
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default ResetPassword;
