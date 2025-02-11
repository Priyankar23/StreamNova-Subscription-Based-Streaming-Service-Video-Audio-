import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/UserDashboard.css';
import Slider from "react-slick";
function UserDashboard() {
  const navigate = useNavigate();
  const token = localStorage.getItem("jwtToken");
  const userId = localStorage.getItem("userId");
  const [username, setUsername] = useState('');
  const [contentList, setContentList] = useState([]);
  const [hasSubscription, setHasSubscription] = useState(false);
  const [remainingDays, setRemainingDays] = useState(0);
  const [isDropdownVisible, setIsDropdownVisible] = useState(false);
  const [userDetails, setUserDetails] = useState({ username: '', email: '', phoneNumber: '' });
  const [showToast, setShowToast] = useState(false);  // Toast state

  useEffect(() => {
    const username = localStorage.getItem('username') || 'User';
    const email = localStorage.getItem('email') || 'Not updated';
    const phoneNumber = localStorage.getItem('phoneNumber') || 'Not updated';

    setUserDetails({ username, email, phoneNumber });
  }, []);

  const toggleDropdown = () => {
    setIsDropdownVisible(!isDropdownVisible);
  };

  const handleEditProfile = () => {
    navigate('/profile-update');
  };

  const fetchContent = async () => {
    try {
      const response = await fetch("http://localhost:8082/content", {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch content');
      }

      const data = await response.json();
      setContentList(data);
    } catch (error) {
      console.error("Error fetching content:", error);
      alert("Error fetching content. Please try again later.");
    }
  };

  const checkSubscriptionStatus = async () => {
    try {
      const response = await fetch(`http://localhost:8082/subscriptions/check/${userId}`, {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to check subscription status');
      }

      const data = await response.json();
      setHasSubscription(data);
    } catch (error) {
      console.error("Error checking subscription:", error);
      alert("Error checking subscription. Please try again later.");
    }
  };

  const fetchRemainingDays = async () => {
    try {
      const response = await fetch(`http://localhost:8082/subscriptions/remaining-days/${userId}`, {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch remaining days');
      }

      const days = await response.json();
      setRemainingDays(days);
      
      // Show toast if subscription is about to expire (e.g., within 5 days)
      if (days <= 5) {
        setShowToast(true);
        setTimeout(() => {
          setShowToast(false); // Hide toast after 5 seconds
        }, 5000);
      }
    } catch (error) {
      console.error("Error fetching remaining days:", error);
    }
  };

  useEffect(() => {
    const userRole = localStorage.getItem("userRole");

    if (userRole !== "ROLE_USER") {
      navigate("/login");
    } else {
      const storedUsername = localStorage.getItem("username");
      setUsername(storedUsername || 'User');
      fetchContent();
      checkSubscriptionStatus();
      fetchRemainingDays();
    }
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("userId");
    localStorage.removeItem("username");
    navigate('/');
  };

  const handleStreamContent = async (content) => {
    try {
      if (!token || !userId) {
        alert("Authentication details are missing. Please log in again.");
        navigate('/login');
        return;
      }

      if (!hasSubscription) {
        alert("You must subscribe to stream content.");
        navigate('/user-subscriptions');
        return;
      }

      const filename = content.url.split('/').pop(); 
      const encodedFilename = encodeURIComponent(filename);

      const response = await fetch(`http://localhost:8082/stream/media/${encodedFilename}?userId=${userId}&contentId=${content.id}`, {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch media file');
      }

      const mediaUrl = response.url;
	  navigate("/content-stream", { state: { streamingUrl: mediaUrl, imageUrl: content.imageUrl } });

    } catch (error) {
      console.error("Error streaming content:", error);
      alert("An error occurred while preparing the streaming URL.");
    }
  };

  const groupContentByGenre = () => {
    return contentList.reduce((acc, content) => {
      const { genre } = content;
      if (!acc[genre]) acc[genre] = [];
      acc[genre].push(content);
      return acc;
    }, {});
  };

  const contentByGenre = groupContentByGenre();
  const settings = {
      dots: true,
      infinite: false,
      speed: 500,
      slidesToShow: 1,
      slidesToScroll: 1,
      responsive: [
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1,
          },
        },
      ],
    };


  return (
    <div className='dashboard-page'>
      <div className="top-bar">
	  <button onClick={handleLogout}>Logout</button>
	        <button onClick={() => navigate('/profile-update')}>Update Profile</button>
	        <button onClick={() => navigate('/user-subscriptions')}>Manage Subscriptions</button>
	        <button onClick={() => navigate('/recommendations')}>View Recommendations</button>
        <img
          src="/images/profile.png"
          alt="Profile"
          className="profile-img"
          onClick={toggleDropdown}
        />
        {isDropdownVisible && (
          <div className="dropdown">
            <p><strong>Username:</strong> {userDetails.username}</p>
            <p><strong>Email:</strong> {userDetails.email}</p>
            <p><strong>Phone Number:</strong> {userDetails.phoneNumber}</p>
            <button onClick={handleEditProfile}>Edit Profile</button>
          </div>
        )}
      </div>

      <h2>Welcome {username || 'User'}!</h2>
      

	  {Object.keys(contentByGenre).length > 0 ? (
	    Object.keys(contentByGenre).map((genre) => (
	      <div key={genre} className="genre-section">
	        <h4>{genre}</h4>
	        <Slider {...settings}>
	          {contentByGenre[genre].map((content) => (
	            <div key={content.id} className="content-item">
				<div>
				<p className='title'><strong>{content.title}</strong></p>
	              {content.imageUrl && (
	                <img
	                  src={content.imageUrl}
	                  alt={`${content.title}`}
	                  className="content-img"
	                />
	              )}
				  </div>
	              <div className="content-details">
	                
					
	                <p>{content.description}</p>
	                <p className='star'>
					<div>
	                  <img src="/images/star.png" alt="star" className="star-icon" />
					  </div>
					  <div>
	                  <strong className='title'>Rating:</strong> {content.rating}
					  </div>
	                </p>
	                <button onClick={() => handleStreamContent(content)} >Stream Content</button>
	              </div>
	            </div>
	          ))}
	        </Slider>
	      </div>
	    ))
	  ) : (
	    <p>No content available at the moment.</p>
	  )}


      {/* Toast Notification */}
      {showToast && (
        <div className="toast">
          <p>Your subscription will expire in {remainingDays} days.</p>
        </div>
      )}
    </div>
  );
}

export default UserDashboard;
