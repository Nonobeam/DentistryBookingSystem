import React, { useState } from 'react';
import axios from 'axios';
import { Form, Input, Button, Typography, Alert } from 'antd';

const { Title } = Typography;

const ForgotPassword = () => {
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const onFinish = async (values) => {
    console.log('Success:', values);
    try {
      const response = await axios.post(`http://localhost:8080/user/forgotPassword?mail=${values.email}`);
      setSuccessMessage("The reset password link has been sent to your email.");
      setErrorMessage(""); 
    } catch (error) {
      setErrorMessage("An error occurred. Please try again later.");
      setSuccessMessage(""); 
    }
  };

  const onFinishFailed = (errorInfo) => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div style={{ display: 'flex', height: '100vh', justifyContent: 'center', alignItems: 'center' }}>
      <div style={{ maxWidth: '500px', width: '100%', padding: '45px', backgroundColor: "ghostwhite", borderRadius: '20px' }}>
        
        <Title>Forgot your password?</Title>
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
            <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
              Send Reset Link
            </Button>
          </Form.Item>

        </Form>
        <div style={{ display: "flex", justifyContent: "flex-end" }}>
          <Button href="/login">Login</Button>
        </div>
        
      </div>
    </div>
  );
};

export default ForgotPassword;
