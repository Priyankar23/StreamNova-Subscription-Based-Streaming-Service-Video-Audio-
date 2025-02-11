import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/TrackPage.css';

function TrackPage() {
  const [trackedActivities, setTrackedActivities] = useState([]);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('jwtToken'); // Retrieve JWT token
    if (!token) {
      setError('Authorization token is missing. Please log in again.');
      setIsLoading(false);
      return;
    }

    const fetchTrackedActivities = async () => {
      try {
        const response = await axios.get(
          'http://localhost:8082/analytics/history', // Updated endpoint
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (Array.isArray(response.data)) {
          // Filter out activities with invalid contentId (e.g., 101)
          const validActivities = response.data.filter(
            (activity) => activity.contentId && activity.contentId !== 101
          );
          setTrackedActivities(validActivities);
        } else {
          setError('Unexpected response format');
        }
      } catch (err) {
        console.error('Error fetching tracked activities:', err);
        if (err.response) {
          if (err.response.status === 403) {
            setError('Access denied: Ensure you have admin privileges.');
          } else {
            setError(`Error: ${err.response.statusText}`);
          }
        } else {
          setError('Network or server error.');
        }
      } finally {
        setIsLoading(false);
      }
    };

    fetchTrackedActivities();
  }, []);

  if (isLoading) {
    return <div className="track-loading">Loading...</div>;
  }

  if (error) {
    return <div className="track-error">Error: {error}</div>;
  }

  return (
    <div className="track-page">
      <button className="track-back-button" onClick={() => navigate('/admin-dashboard')}>
        Back to Dashboard
      </button>
      <h2 className="track-title">Tracked Activities</h2>
      {trackedActivities.length === 0 ? (
        <p className="track-no-activities">No tracked activities found.</p>
      ) : (
        <table className="track-table">
          <thead className="track-table-header">
            <tr>
              <th>User ID</th>
              <th>Content ID</th>
              <th>Timestamp</th>
            </tr>
          </thead>
          <tbody className="track-table-body">
            {trackedActivities.map((activity, index) => (
              <tr key={index} className="track-table-row">
                <td>{activity.userId}</td>
                <td>{activity.contentId}</td>
                <td>{activity.viewTime}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default TrackPage;
