import React from "react";
import { Navigate } from "react-router-dom";
import NotAuthorized from "./NotAuthorized";
import Login from "./Login";

const RoleBasedRoute = ({ element: Component, requiredRole, ...rest }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const expirationTime = localStorage.getItem("expirationTime");

  if (!token || new Date().getTime() > expirationTime) {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("expirationTime");
    return <Login />;
  }

  switch (role) {
    case "CUSTOMER":
      if (requiredRole && !requiredRole.includes(role)) {
        return <NotAuthorized />;
      }
      break;
    case "BOSS":
      if (requiredRole && !requiredRole.includes(role)) {
        return <Navigate to="/boss" />;
      }
      break;
    case "STAFF":
      if (requiredRole && !requiredRole.includes(role)) {
        return <Navigate to="/staff" />;
      }
      break;
    case "ADMIN":
      if (requiredRole && !requiredRole.includes(role)) {
        return <Navigate to="/admin" />;
      }
      break;
    case "MANAGER":
      if (requiredRole && !requiredRole.includes(role)) {
        return <Navigate to="/manager" />;
      }
      break;
    case "DENTIST":
      if (requiredRole && !requiredRole.includes(role)) {
        return <Navigate to="/dentist" />;
      }
      break;
    default:
      return <Navigate to="/" />;
  }

  return Component;
};

export default RoleBasedRoute;
