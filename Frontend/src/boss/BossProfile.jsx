import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { Form, Input, Button, DatePicker, message, Layout, Card, Skeleton } from "antd";
import { UserOutlined, PhoneOutlined, MailOutlined, CalendarOutlined } from '@ant-design/icons';
import dayjs from "dayjs";
import BossSidebar from './BossSideBar';

const { Header, Content } = Layout;

const UserProfile = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [update, setUpdate] = useState(false);
  const [form] = Form.useForm();

  const fetchUserInfo = useCallback(async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/api/v1/boss/info", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      setUser(response.data);
      setLoading(false);
      form.setFieldsValue({
        name: response.data.name,
        phone: response.data.phone,
        mail: response.data.mail,
        birthday: dayjs(response.data.birthday)
      });
    } catch (error) {
      message.error("Failed to fetch user information");
      setLoading(false);
    }
  }, [form]);

 

  useEffect(() => {
    fetchUserInfo();
  }, [fetchUserInfo]);

  const handleUpdate = async (values) => {
    setUpdate(true);
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        "http://localhost:8080/api/v1/boss/info/update",
        {
          name: values.name,
          phone: values.phone,
          mail: user.mail,
          birthday: values.birthday.format("YYYY-MM-DD"),
          id: user.id,
          status: user.status
        },
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );
      setUser(response.data);
      message.success("Profile updated successfully");
    } catch (error) {
      message.error("Failed to update profile");
    } finally {
      setUpdate(false);
    }
  };

  const disableDatesAfterToday = (current) => {
    const today = new Date();
    today.setHours(23, 59, 59, 999);
    return current > today;
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <BossSidebar />
      <Layout style={{ margin: "0 auto", textAlign: "center" }}>
        <Header className="site-layout-sub-header-background">
        </Header>
        <Content style={{ margin: '20px auto', width: '600px' }}>
          <Card
            title="Profile"
            bordered={false}
            style={{ textAlign: "center" }}
          >
            {loading ? (
              <Skeleton active />
            ) : (
              <Form
                form={form}
                style={{ maxWidth: "600px", margin: "0 auto" }}
                layout="vertical"
                onFinish={handleUpdate}
                initialValues={{
                  name: user.name,
                  phone: user.phone,
                  mail: user.mail,
                  birthday: dayjs(user.birthday)
                }}
              >
                <Form.Item
                  name="name"
                  label={<span><UserOutlined /> Name </span>}
                  rules={[{ required: true, message: "Please enter your name" }]}
                >
                  <Input placeholder="Enter your name" />
                </Form.Item>
                <Form.Item
                  name="phone"
                  label={<span><PhoneOutlined /> Phone </span>}
                  rules={[
                    { required: true, message: "Please enter your phone number" },
                    { pattern: /^\d{10}$/, message: "Phone number must be 10 digits!" }
                  ]}
                >
                  <Input placeholder="Enter your phone number" />
                </Form.Item>
                <Form.Item
                  name="mail"
                  label={<span><MailOutlined /> Email</span>}
                  rules={[{ required: true, message: "Please enter your email" }]}
                >
                  <Input disabled />
                </Form.Item>
                <Form.Item
                  name="birthday"
                  label={<span><CalendarOutlined /> Birthday </span>}
                  rules={[{ required: true, message: "Please select your birthday" }]}
                >
                  <DatePicker
                    format={'DD-MM-YYYY'}
                    disabledDate={disableDatesAfterToday}
                    style={{ width: "100%" }}
                  />
                </Form.Item>
                <Form.Item>
                  <a href="/forgot">Wanna change password?</a>
                </Form.Item>
                <Form.Item>
                  <Button type="primary" loading={update} htmlType="submit" style={{ width: "100%" }}>
                    Update Profile
                  </Button>
                </Form.Item>
              </Form>
            )}
          </Card>
        </Content>
      </Layout>
    </Layout>
  );
};

export default UserProfile;
