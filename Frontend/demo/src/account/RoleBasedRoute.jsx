import React from "react";
import { Route, Navigate } from "react-router-dom";
import useAuth from "./useAuth";

const RoleBasedRoute = ({ element: Component, requiredRole, ...rest }) => {
  useAuth(requiredRole); // Call the custom hook with the required role

  return localStorage.getItem("token") ? (
    <Route {...rest} element={Component} />
  ) : (
    <Navigate to="/login" />
  );
};

export default RoleBasedRoute;
