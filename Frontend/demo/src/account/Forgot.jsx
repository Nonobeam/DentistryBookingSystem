import React from 'react';
import { Form, Input, Button, Typography } from 'antd';

const { Title } = Typography;

const ForgotPassword = () => {
  
  const onFinish = values => {
    console.log('Success:', values);
  };

  const onFinishFailed = errorInfo => {
    console.log('Failed:', errorInfo);
  };

  return (
    <div style={{ display: 'flex', height: '100vh', justifyContent: 'center', alignItems: 'center' }}>
      <div style={{ maxWidth: '500px', width: '100%', padding: '45px', backgroundColor: "ghostwhite", borderRadius: '20px' }}>
        
        <Title>Forgot your password?</Title>
        <p>We'll send you an email to help you reset your password.</p>

        <Form
          name="forgotPassword"
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
        >
          <Form.Item
            name="email"
            rules={[{ required: true, message: 'Please input your email address!' },
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
        <div style={{display: "flex", justifyContent: "flex-end"}}>
        <Button href="/login">Login</Button></div>
        
      </div>
    </div>
  );
};

export default ForgotPassword;
