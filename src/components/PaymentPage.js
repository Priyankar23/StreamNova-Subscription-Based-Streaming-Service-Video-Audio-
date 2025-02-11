import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/PaymentPage.css'; // Import the new CSS file

function PaymentPage() {
  const location = useLocation();
  const navigate = useNavigate();
  const { subscriptionId, name, price, durationInDays, isRenewal } = location.state || {};
  const [transactionId, setTransactionId] = useState(null);

  const generateTransactionId = () => {
    return `TXN${Date.now()}${Math.floor(Math.random() * 1000)}`;
  };

  const handlePaymentSubmit = async (e) => {
    e.preventDefault();
    const txnId = generateTransactionId();
    setTransactionId(txnId);

    const jwtToken = localStorage.getItem("jwtToken");
    if (!jwtToken) {
      alert("You are not authenticated. Please log in again.");
      navigate("/login");
      return;
    }

    try {
      const response = await fetch(
        isRenewal
          ? "http://localhost:8082/subscriptions/renew"
          : "http://localhost:8082/subscriptions/purchase",
        {
          method: isRenewal ? "PUT" : "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${jwtToken}`,
          },
          body: JSON.stringify({
            userId: localStorage.getItem("userId"),
            subscriptionId,
            paymentMethod: "credit_card",
            amount: price,
            transactionId: txnId,
          }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Payment failed.");
      }

      alert(isRenewal ? "Renewal successful!" : "Purchase successful!");
      navigate('/payment-confirmation', {
        state: { subscriptionId, name, price, durationInDays, transactionId: txnId, isRenewal },
      });
    } catch (error) {
      alert(`Payment failed. ${error.message}`);
    }
  };

  return (
    <div className="payment-container">
      <h2 className="payment-title">
        {isRenewal ? `Renew Subscription: ${name}` : `Payment for ${name}`}
      </h2>
      <div className="subscription-details">
        <ul>
          <li>Subscription: {name}</li>
          <li>Price: ${price}</li>
          <li>Duration: {durationInDays} days</li>
        </ul>
      </div>
      <form className="payment-form" onSubmit={handlePaymentSubmit}>
        <label htmlFor="cardNumber">Card Number:</label>
        <input type="text" id="cardNumber" name="cardNumber" placeholder="Enter card number" required />
        <label htmlFor="expiryDate">Expiry Date:</label>
        <input type="text" id="expiryDate" name="expiryDate" placeholder="MM/YY" required />
        <label htmlFor="cvv">CVV:</label>
        <input type="text" id="cvv" name="cvv" placeholder="Enter CVV" required />
        <button type="submit">Pay Now</button>
      </form>
      <button className="back-dashboard-button" onClick={() => navigate('/user-dashboard')}>
        Back to Dashboard
      </button>
    </div>
  );
}

export default PaymentPage;
