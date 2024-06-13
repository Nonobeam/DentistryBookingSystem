import React from "react";
import "antd/dist/reset.css"
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

import Signup from "./account/Signup";
import Login from "./account/Login";
import ForgotPassword from "./account/Forgot";
import Services from "./homepage/Services";
import Educational from "./homepage/Educational";
import Booking from "./homepage/Booking";
import Homepage from "./homepage/Homepage";


const App = () => {
  return (
    <Router>
      <Routes>
          <Route exact path="/" element={<Homepage />} />
          <Route exact path="/booking" element={<Booking />} />
          <Route path="/educational" element={<Educational />} />
          <Route path="/services" element={<Services />} />
          <Route path="/signup" element={<Signup />} />
          <Route path="/login" element={<Login />} />
          <Route path="/forgot" element={<ForgotPassword />} />
        </Routes>
    
    </Router>
  );
};

export default App;
