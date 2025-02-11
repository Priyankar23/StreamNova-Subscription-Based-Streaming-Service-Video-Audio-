import React, { useState, useEffect } from 'react';  
import { useNavigate } from 'react-router-dom';
import '../styles/ProfileUpdateForm.css'; // Import the new CSS file

function ProfileUpdateForm() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [updateSuccess, setUpdateSuccess] = useState(false);
  const token = localStorage.getItem("jwtToken");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();

    const userId = localStorage.getItem("userId");
    if (!userId) {
      alert("User ID not found in local storage.");
      return;
    }

    const updatedProfile = { userId, username, email, password, phoneNumber };

    try {
      const response = await fetch("http://localhost:8082/auth/profile", {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(updatedProfile),
      });

      if (response.ok) {
        localStorage.setItem('username', username);
        setUpdateSuccess(true); // Set the success state
      } else {
        const responseData = await response.json();
        alert("Error updating profile: " + responseData.message);
      }
    } catch (error) {
      console.error("Error during profile update:", error);
      alert("An error occurred while updating the profile.");
    }
  };

  useEffect(() => {
    if (updateSuccess) {
      alert("Profile updated successfully!");
	  localStorage.setItem('username', username);
	      localStorage.setItem('email', email);
	      localStorage.setItem('phoneNumber', phoneNumber); // Show success alert
      navigate('/user-dashboard'); // Redirect to the dashboard after the update
    }
  }, [updateSuccess, navigate]);

  const handleBackToDashboard = () => {
    navigate('/user-dashboard');
  };

  return (
    <div className="profile-update">
      <div className="profile-container">
        <h2>Update Profile</h2>
        <form className="profile-form" onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Phone Number"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            required
          />
          <button type="submit">Update Profile</button>
        </form>
        <button className="back-to-dashboard-btn" onClick={handleBackToDashboard}>
          Back to Dashboard
        </button>
      </div>
    </div>
  );
}

export default ProfileUpdateForm;
