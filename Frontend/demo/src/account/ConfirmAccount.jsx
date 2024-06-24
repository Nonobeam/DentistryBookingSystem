import React, { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';

const ConfirmAccount = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const confirmAccount = async () => {
      const queryParams = new URLSearchParams(location.search);
      const token = queryParams.get('token');

      if (!token) {
        navigate('/'); // no token is present
        return;
      }

      try {
        await axios.get(`http://localhost:8080/api/v1/auth/confirm?token=${token}`);
        navigate('/login', { state: { message: 'Confirmation successful, please login' } });
      } 
      
      catch (error) {
        console.error('Account confirmation failed:', error);
        navigate('/login', { state: { errormessage: 'Something went wrong' } }); 
      }
    };

    confirmAccount();
  }, [location, navigate]);

  return <div>Confirming your account...</div>;
};

export default ConfirmAccount;
