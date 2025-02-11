import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import '../styles/ContentStreamPage.css';

function ContentStreamPage() {
    const location = useLocation();
    const navigate = useNavigate();
    const [streamingUrl, setStreamingUrl] = useState(location.state?.streamingUrl || "");
    const [imageUrl, setImageUrl] = useState(location.state?.imageUrl || ""); // Get image URL from state

    useEffect(() => {
        if (!streamingUrl) {
            alert("No streaming URL provided. Redirecting to the home page.");
            navigate("/user-dashboard");
        }
    }, [streamingUrl, navigate]);

    // Remove query parameters to extract the file extension
    const cleanUrl = streamingUrl.split('?')[0];

    // Detect media type based on URL extension (ignoring query parameters)
    const isVideo = cleanUrl && /\.(mp4|webm|ogg|avi)$/i.test(cleanUrl);
    const isAudio = cleanUrl && /\.(mp3|wav|flac|ogg)$/i.test(cleanUrl);

    return (
		<div className="streaming-page">
		    <h2 className="streaming-title">Content Streaming</h2>

		    {isAudio && imageUrl && (
		        <div className="audio-image-container">
		            <img 
		                src={imageUrl} 
		                alt="Audio Content Cover" 
		                className="audio-image" 
		            />
		        </div>
		    )}

		    {isVideo && (
		        <div className="video-player-container">
		            <h3>Streaming Video</h3>
		            <video controls>
		                <source src={streamingUrl} type="video/mp4" />
		                <source src={streamingUrl} type="video/webm" />
		                <source src={streamingUrl} type="video/ogg" />
		                Your browser does not support the video tag.
		            </video>
		        </div>
		    )}

		    {isAudio && (
		        <div className="audio-player-container">
				<div>
		            <h3>Streaming Audio</h3>
					</div>
					<div>
		            <audio controls>
		                <source src={streamingUrl} type="audio/mp3" />
		                <source src={streamingUrl} type="audio/ogg" />
		                <source src={streamingUrl} type="audio/wav" />
		                Your browser does not support the audio tag.
		            </audio>
					</div>
		        </div>
		    )}

		    {!isVideo && !isAudio && streamingUrl && (
		        <p className="fallback-message">Unsupported media format. Please try a different file.</p>
		    )}

		    {!streamingUrl && (
		        <p className="fallback-message">No media found to stream. Please go back and try again.</p>
		    )}

		    <button className="back-button" onClick={() => navigate("/user-dashboard")}>Back</button>
		</div>

    );
}

export default ContentStreamPage;
