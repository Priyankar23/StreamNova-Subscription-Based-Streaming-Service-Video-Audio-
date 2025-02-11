import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/PaymentConfirmationPage.css'; // Import the stylesheet

function PaymentConfirmationPage() {
  const location = useLocation();
  const navigate = useNavigate();

  const {
    subscriptionId,
    name,
    price,
    durationInDays,
    transactionId,
    isRenewal,
  } = location.state || {};

  const handleBackToDashboard = () => {
    navigate('/user-dashboard', {
      state: {
        updatedSubscription: { id: subscriptionId, durationInDays },
      },
    });
  };

  return (
    <div className="confirmation-container">
      <h2 className="confirmation-title">
        {isRenewal ? "Renewal Successful!" : "Payment Successful!"}
      </h2>
      <p className="confirmation-message">
        Thank you for your {isRenewal ? "renewal" : "purchase"}.
      </p>
      <ul className="confirmation-details">
        <li className="confirmation-detail-item">Subscription: {name}</li>
        <li className="confirmation-detail-item">Price: ${price}</li>
        <li className="confirmation-detail-item">Duration: {durationInDays} days</li>
        <li className="confirmation-detail-item">Transaction ID: {transactionId}</li>
      </ul>
      <button className="dashboard-return-button" onClick={handleBackToDashboard}>
        Back to Dashboard
      </button>
    </div>
  );
}

export default PaymentConfirmationPage;
