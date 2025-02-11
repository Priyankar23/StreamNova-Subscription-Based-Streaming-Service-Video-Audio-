import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import AdminDashboard from './components/AdminDashboard';
import UserDashboard from './components/UserDashboard';
import ProfileUpdateForm from './components/ProfileUpdateForm';
import PaymentPage from './components/PaymentPage';
import PaymentConfirmationPage from './components/PaymentConfirmationPage';
import SubscriptionManagement from './components/SubscriptionManagement';
import ContentManagement from './components/ContentManagement';
import UserSubscriptions from './components/UserSubscriptions';
import ContentStreamPage from './components/ContentStreamPage';
import TrackPage from './components/TrackPage';
import RecommendationsPage from './components/RecommendationsPage';
import StreamNovaPage from './components/StreamNovaPage';
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

function App() {
  return (
    <Router>
      <Routes>
	  <Route path="/" element={<StreamNovaPage />} />
	         <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/admin-dashboard" element={<AdminDashboard />} />
        <Route path="/subscriptions" element={<SubscriptionManagement />} />
        <Route path="/content" element={<ContentManagement />} />
        <Route path="/user-dashboard" element={<UserDashboard />} />
        <Route path="/user-subscriptions" element={<UserSubscriptions />} />
        <Route path="/profile-update" element={<ProfileUpdateForm />} />
        <Route path="/payment" element={<PaymentPage />} />
        <Route path="/payment-confirmation" element={<PaymentConfirmationPage />} />
		<Route path="/content-stream" element={<ContentStreamPage />} />
		<Route path="/track" element={<TrackPage />} />
		<Route path="/recommendations" element={<RecommendationsPage />} />
      </Routes>
    </Router>
  );
}

export default App;
