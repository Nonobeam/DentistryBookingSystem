import React from "react";
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

  if (requiredRole && !requiredRole.includes(role)) {
    return <NotAuthorized/>;
  }

  return Component;
};

export default RoleBasedRoute;
