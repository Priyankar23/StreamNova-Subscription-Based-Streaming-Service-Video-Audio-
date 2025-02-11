# StreamNova-Subscription-Based-Streaming-Service-Video-Audio-

StreamNova is a **subscription-based streaming platform** for video and audio content. It allows users to register, subscribe, and access high-quality streaming content, while admins manage content and subscriptions.

## Features

###  User Features
- **User Registration & Login** – Secure authentication for users.
- **Subscription Management** – Purchase and renew subscriptions.
- **Content Streaming** – Watch or listen to high-quality content.
- **Payment Gateway** – Mock integration for handling subscriptions.
- **User Dashboard** – Personalized interface with recommendations.

### 🛠️ Admin Features
- **Admin Dashboard** – Manage users, subscriptions, and content.
- **Content Upload** – Add video/audio content.
- **Category Management** – Organize content into genres.
- **Analytics & Reports** – Track user engagement and revenue.

## Tech Stack

### Backend (Spring Boot)
- **Spring Boot** – Backend framework
- **MySQL** – Database for storing user and content data
- **Spring Security** – Authentication & authorization
- **REST APIs** – For communication between frontend & backend

### Frontend (React.js)
- **React.js** – UI development
- **Tailwind CSS** – Styling
- **Axios** – API calls

  1. Clone the repository:
   ```sh
   git clone https://github.com/Priyankar23/StreamNova-Subscription-Based-Streaming-Service-Video-Audio-.git
   ```
2. Navigate to the backend folder:
   ```sh
   cd SubscriptionBasedStreamingService
   ```
3. Build and run the project:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
4. Backend runs on `http://localhost:8082`

### Frontend Setup
1. Navigate to the React app directory:
   ```sh
   cd reactapp
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the React app:
   ```sh
   npm start
   ```
4. Frontend runs on `http://localhost:3000`
