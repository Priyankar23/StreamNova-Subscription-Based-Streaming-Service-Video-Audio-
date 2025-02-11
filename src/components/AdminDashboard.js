import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/AdminDashboard.css';

function AdminDashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    document.body.classList.add('admin-dashboard');
    return () => {
      document.body.classList.remove('admin-dashboard');
    };
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    navigate('/');
  };

  return (
    <div className="admin-dashboard-container">
      <h2>Welcome Admin</h2>
      <button onClick={handleLogout}>Logout</button>
      <button onClick={() => navigate('/subscriptions')}>Manage Subscriptions</button>
      <button onClick={() => navigate('/content')}>Manage Content</button>
      <button onClick={() => navigate('/track')}>View Tracked Activities</button>
    </div>
  );
}

export default AdminDashboard;
