import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/RecommendationsPage.css'; // Import the CSS file

function RecommendationsPage() {
    const [recommendations, setRecommendations] = useState([]);
    const token = localStorage.getItem("jwtToken");
    const userId = localStorage.getItem("userId");
    const navigate = useNavigate();
    const [hasSubscription, setHasSubscription] = useState(false);

    useEffect(() => {
        const fetchRecommendations = async () => {
            try {
                const response = await fetch(`http://localhost:8082/analytics/recommendations/${userId}`, {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                    },
                });

                if (!response.ok) {
                    throw new Error("Failed to fetch recommendations");
                }

                const data = await response.json();
                setRecommendations(data);
            } catch (error) {
                console.error("Error fetching recommendations:", error);
                alert("Error fetching recommendations. Please try again later.");
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

        fetchRecommendations();
        checkSubscriptionStatus();
    }, [token, userId]);

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

    return (
        <div className="recommendations-container">
            <button onClick={() => navigate('/user-dashboard')}>Back to Dashboard</button>
            <h2>Recommended for You</h2>
            <ul>
                {recommendations.length > 0 ? (
                    recommendations.map((content) => (
                        <li key={content.id}>
                            {content.imageUrl && (
                                <div>
                                    <img
                                        src={content.imageUrl}
                                        alt={`${content.title}`}
                                        style={{ width: '100px', height: '100px' }}
                                    />
                                </div>
                            )}
                            <p><strong>{content.title}</strong>: {content.description}</p>
                            <p><strong>Genre:</strong> {content.genre}</p>
                            <p><img src="/images/star.png" alt="star" style={{ width: '16px', height: '16px', verticalAlign: 'middle' }} /><strong>Rating:</strong> {content.rating} {' '}</p>
                            <button onClick={() => handleStreamContent(content)}>Stream Content</button>
                        </li>
                    ))
                ) : (
                    <p>No recommendations available at the moment.</p>
                )}
            </ul>
        </div>
    );
}

export default RecommendationsPage;
