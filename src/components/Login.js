import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import '../styles/Login.css'; // CSS for login page

function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isAdmin, setIsAdmin] = useState(false); // Toggle for Admin login
  const navigate = useNavigate();

  // Add the "login-page" class to <body> when the component is mounted
  useEffect(() => {
    document.body.classList.add('login-page');
    return () => {
      document.body.classList.remove('login-page');
    };
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();

    const loginData = { username, password };
    const endpoint = isAdmin
      ? "http://localhost:8082/auth/admin-login"
      : "http://localhost:8082/auth/user-login";

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData),
      });

      const responseData = await response.json();

      if (response.ok) {
        // Store necessary data in localStorage
        localStorage.setItem("jwtToken", responseData.token);
        localStorage.setItem("userRole", responseData.role);
        localStorage.setItem("userId", responseData.userId);
        localStorage.setItem("username", responseData.username); // Save username

        // Redirect user based on role
        navigate(responseData.role === "ROLE_ADMIN" ? "/admin-dashboard" : "/user-dashboard");
      } else {
        alert("Login failed: " + responseData.message);
      }
    } catch (error) {
      console.error("Error during login:", error);
      alert("An error occurred during login. Please try again.");
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
        <h2>Login</h2>
        <input
          type="text"
          placeholder="Username"
          value={username}
          autoComplete="username"
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          autoComplete="current-password"
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <div className="admin-toggle">
          <label>
            Admin Login
            <input
              type="checkbox"
              checked={isAdmin}
              onChange={() => setIsAdmin(!isAdmin)}
            />
          </label>
        </div>
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register here</Link>
      </p>
    </div>
  );
}

export default Login;
