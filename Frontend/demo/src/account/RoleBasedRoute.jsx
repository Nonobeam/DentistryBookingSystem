import React from "react";
import { Route, Navigate } from "react-router-dom";
import NotAuthorized from "./NotAuthorized";

const RoleBasedRoute = ({ element: Component, requiredRole, ...rest }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const expirationTime = localStorage.getItem("expirationTime");

  if (!token || new Date().getTime() > expirationTime) {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("expirationTime");
    return <Navigate to="/login" />;
  }

  if (requiredRole && role !== requiredRole) {
    return <NotAuthorized/>;
  }

  return Component;
};

export default RoleBasedRoute;
