import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/SubscriptionManagement.css'; // Ensure this path matches your project structure

function SubscriptionManagement() {
  const navigate = useNavigate();
  const token = localStorage.getItem("jwtToken");
  const [subscriptions, setSubscriptions] = useState([]);
  const [newSubscription, setNewSubscription] = useState({
    name: '',
    description: '',
    price: '',
    durationInDays: '',
  });
  const [editMode, setEditMode] = useState(false);
  const [editingSubscription, setEditingSubscription] = useState(null);

  const fetchSubscriptions = async () => {
    try {
      const response = await fetch("http://localhost:8082/subscriptions", {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error(`Failed to fetch subscriptions: ${response.statusText}`);
      }

      const responseBody = await response.text();
      if (responseBody) {
        const data = JSON.parse(responseBody);
        setSubscriptions(data);
      } else {
        console.warn("No subscriptions data received.");
      }
    } catch (error) {
      console.error("Error fetching subscriptions:", error);
      alert("Error fetching subscriptions. Please try again.");
    }
  };

  const handleCreateSubscription = async () => {
    const response = await fetch("http://localhost:8082/subscriptions", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify(newSubscription),
    });
    if (response.ok) {
      alert('Subscription created successfully');
      fetchSubscriptions();
      setNewSubscription({
        name: '',
        description: '',
        price: '',
        durationInDays: '',
      });
    } else {
      alert('Failed to create subscription');
    }
  };

  const handleEditSubscription = async () => {
    const response = await fetch(`http://localhost:8082/subscriptions/${editingSubscription.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify(newSubscription),
    });
    if (response.ok) {
      alert('Subscription updated successfully');
      setEditMode(false);
      fetchSubscriptions();
      setEditingSubscription(null);
      setNewSubscription({
        name: '',
        description: '',
        price: '',
        durationInDays: '',
      });
    } else {
      alert('Failed to update subscription');
    }
  };

  const handleDeleteSubscription = async (id) => {
    const response = await fetch(`http://localhost:8082/subscriptions/${id}`, {
      method: "DELETE",
      headers: {
        "Authorization": `Bearer ${token}`,
      },
    });
    if (response.ok) {
      alert('Subscription deleted successfully');
      fetchSubscriptions();
    } else {
      alert('Failed to delete subscription');
    }
  };

  useEffect(() => {
    const userRole = localStorage.getItem("userRole");

    if (userRole !== "ROLE_ADMIN") {
      navigate("/login");
    }
	document.body.classList.add("subscription-management");
    fetchSubscriptions();
	return () => {
	   document.body.classList.remove("subscription-management");
	 };
  }, [navigate]);

  const handleEditClick = (subscription) => {
    setEditMode(true);
    setEditingSubscription(subscription);
    setNewSubscription({
      name: subscription.name,
      description: subscription.description,
      price: subscription.price,
      durationInDays: subscription.durationInDays,
    });
  };

  const handleBackToDashboard = () => {
    navigate('/admin-dashboard');
  };

  return (
    <div className="subscription-container">
      <h2>Manage Subscriptions</h2>

      <div className="subscription-form">
        <h3>{editMode ? "Edit Subscription" : "Create Subscription"}</h3>
        <input
          type="text"
          placeholder="Subscription Name"
          value={newSubscription.name}
          onChange={(e) => setNewSubscription({ ...newSubscription, name: e.target.value })}
        />
        <input
          type="text"
          placeholder="Description"
          value={newSubscription.description}
          onChange={(e) => setNewSubscription({ ...newSubscription, description: e.target.value })}
        />
        <input
          type="number"
          placeholder="Price"
          value={newSubscription.price}
          onChange={(e) => setNewSubscription({ ...newSubscription, price: e.target.value })}
        />
        <input
          type="number"
          placeholder="Duration in Days"
          value={newSubscription.durationInDays}
          onChange={(e) => setNewSubscription({ ...newSubscription, durationInDays: e.target.value })}
        />
        <button onClick={editMode ? handleEditSubscription : handleCreateSubscription}>
          {editMode ? "Save Changes" : "Create Subscription"}
        </button>
      </div>

      <h3>Subscription List</h3>
      <ul className="subscription-list">
        {subscriptions.map((subscription) => (
          <li key={subscription.id}>
            <p>
              {subscription.name} - ₹{subscription.price} - {subscription.durationInDays} days
            </p>
            <div>
              <button onClick={() => handleEditClick(subscription)}>Edit</button>
              <button onClick={() => handleDeleteSubscription(subscription.id)}>Delete</button>
            </div>
          </li>
        ))}
      </ul>

      <button className="back-to-dashboard-btn" onClick={handleBackToDashboard}>
        Back to Dashboard
      </button>
    </div>
  );
}

export default SubscriptionManagement;
