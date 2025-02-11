import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/StreamNovaPage.css'; // CSS file for styling

function StreamNovaPage() {
  return (
    <div className="streamnova-page">
      {/* Logo Section */}
     
    
      {/* Button Group */}
      <div className="button-group">
        <Link to="/login">
          <button className="btn login-btn">Login</button>
        </Link>
        <Link to="/register">
          <button className="btn register-btn">Register</button>
        </Link>
      </div>
    </div>
  );
}

export default StreamNovaPage;
