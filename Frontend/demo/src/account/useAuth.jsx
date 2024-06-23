import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

const useAuth = (requiredRole) => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");
    const expirationTime = localStorage.getItem("expirationTime");

    if (!token || new Date().getTime() > expirationTime) {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
      localStorage.removeItem("expirationTime");
      navigate("/login");
    } else if (requiredRole && role !== requiredRole) {
      navigate("/not-authorized");
    }
  }, [navigate, requiredRole]);
};

export default useAuth;
