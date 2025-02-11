import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/UserSubscriptions.css';

function UserSubscriptions() {
  const navigate = useNavigate();
  const [subscriptions, setSubscriptions] = useState([]);

  useEffect(() => {
    fetchSubscriptions();
  }, []);

  const fetchSubscriptions = async () => {
    try {
      const response = await fetch("http://localhost:8082/subscriptions", {
        headers: {
          "Authorization": `Bearer ${localStorage.getItem("jwtToken")}`,
        },
      });

      if (!response.ok) {
        throw new Error('Failed to fetch subscriptions');
      }

      const data = await response.json();
      setSubscriptions(data);
    } catch (error) {
      console.error("Error fetching subscriptions:", error);
      alert("Error fetching subscriptions. Please try again.");
    }
  };

  const handlePurchase = (subscriptionId) => {
    const selectedSubscription = subscriptions.find(sub => sub.id === subscriptionId);

    if (selectedSubscription) {
      navigate('/payment', {
        state: {
          subscriptionId: selectedSubscription.id,
          name: selectedSubscription.name,
          price: selectedSubscription.price,
          durationInDays: selectedSubscription.durationInDays,
        },
      });
    }
  };

  const handleRenew = (subscriptionId) => {
    const selectedSubscription = subscriptions.find(sub => sub.id === subscriptionId);

    if (selectedSubscription) {
      navigate('/payment', {
        state: {
          subscriptionId: selectedSubscription.id,
          name: selectedSubscription.name,
          price: selectedSubscription.price,
          durationInDays: selectedSubscription.durationInDays,
          isRenewal: true,
        },
      });
    }
  };

  return (
    <div className="subscriptions-container">
      <h2 className="subscriptions-title">Manage Subscriptions</h2>
      <button className="dashboard-button" onClick={() => navigate('/user-dashboard')}>
        Back to Dashboard
      </button>

      <ul className="subscriptions-list">
        {subscriptions.map((subscription) => (
          <li key={subscription.id} className="subscription-item">
            <p className="subscription-details">
              <strong>{subscription.name}</strong> - ${subscription.price} - {subscription.durationInDays} days
            </p>
            <button className="subscription-button" onClick={() => handlePurchase(subscription.id)}>
              Purchase
            </button>
            <button className="subscription-button renew-button" onClick={() => handleRenew(subscription.id)}>
              Renew
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default UserSubscriptions;
