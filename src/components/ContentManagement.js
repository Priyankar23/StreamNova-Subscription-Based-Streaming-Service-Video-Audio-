import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import '../styles/ContentManagement.css'; // Import the CSS file for styling

function ContentManagement() {
  const navigate = useNavigate();
  const token = localStorage.getItem("jwtToken");
  const [contents, setContents] = useState([]);
  const [newContent, setNewContent] = useState({
    title: '',
    description: '',
    genre: '',
    rating: '',
    url: '',
  });
  const [editMode, setEditMode] = useState(false);
  const [editingContent, setEditingContent] = useState(null);
  const [selectedImage, setSelectedImage] = useState(null);
  const [selectedFile, setSelectedFile] = useState(null);

  const fetchContent = async () => {
    try {
      const response = await fetch("http://localhost:8082/content", {
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to fetch content");
      }

      const data = await response.json();
      setContents(data);
    } catch (error) {
      console.error("Error fetching content:", error);
      alert("Error fetching content.");
    }
  };

  const handleCreateContent = async () => {
    try {
      const response = await fetch("http://localhost:8082/content", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(newContent),
      });
      if (response.ok) {
        alert("Content created successfully");
        fetchContent();
      } else {
        alert("Failed to create content");
      }
    } catch (error) {
      console.error("Error creating content:", error);
    }
  };

  const handleEditContent = async () => {
    const updatedContent = {
      ...newContent,
      imageUrl: editingContent.imageUrl || newContent.imageUrl,
    };

    try {
      const response = await fetch(`http://localhost:8082/content/${editingContent.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(updatedContent),
      });

      if (response.ok) {
        alert("Content updated successfully");
        setEditMode(false);
        fetchContent();
        setEditingContent(null);
      } else {
        alert("Failed to update content");
      }
    } catch (error) {
      console.error("Error updating content:", error);
    }
  };

  const handleDeleteContent = async (id) => {
    try {
      const response = await fetch(`http://localhost:8082/content/${id}`, {
        method: "DELETE",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
      });
      if (response.ok) {
        alert("Content deleted successfully");
        fetchContent();
      } else {
        alert("Failed to delete content");
      }
    } catch (error) {
      console.error("Error deleting content:", error);
    }
  };

  const handleImageUpload = async (contentId) => {
    if (!selectedImage) {
      alert("Please select an image to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedImage);
    formData.append("contentId", contentId);

    try {
      const response = await fetch("http://localhost:8082/content/uploadimage", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        alert("Image uploaded successfully.");
        fetchContent();
      } else {
        const errorText = await response.text();
        console.error("Backend error:", errorText);
        alert("Failed to upload image.");
      }
    } catch (error) {
      console.error("Error uploading image:", error);
      alert("An error occurred while uploading the image.");
    }
  };

  const handleMediaUpload = async (contentId) => {
    if (!selectedFile) {
      alert("Please select a file to upload.");
      return;
    }

    const formData = new FormData();
    formData.append("file", selectedFile);
    formData.append("contentId", contentId);

    try {
      const response = await fetch("http://localhost:8082/content/uploadMedia", {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`,
        },
        body: formData,
      });

      if (response.ok) {
        alert("Media uploaded successfully.");
        fetchContent();
      } else {
        const errorText = await response.text();
        console.error("Backend error:", errorText);
        alert("Failed to upload media.");
      }
    } catch (error) {
      console.error("Error uploading media:", error);
      alert("An error occurred while uploading the media.");
    }
  };

  useEffect(() => {
    const userRole = localStorage.getItem("userRole");

    if (userRole !== "ROLE_ADMIN") {
      navigate("/login");
    }

    fetchContent();
  }, [navigate]);

  const handleEditClick = (content) => {
    setEditMode(true);
    setEditingContent(content);
    setNewContent({
      title: content.title,
      description: content.description,
      genre: content.genre,
      rating: content.rating,
      url: content.url,
    });
  };

  const handleBackToDashboard = () => {
    navigate('/admin-dashboard');
  };

  return (
    <div className="content-management-container">
      <h2 className="page-title">Manage Content</h2>
      <button className="back-btn" onClick={handleBackToDashboard}>Back to Dashboard</button>

      <div className="form-container">
        <h3>{editMode ? "Edit Content" : "Create Content"}</h3>
        <input
          type="text"
          placeholder="Title"
          value={newContent.title}
          onChange={(e) => setNewContent({ ...newContent, title: e.target.value })}
          className="form-input"
        />
        <input
          type="text"
          placeholder="Description"
          value={newContent.description}
          onChange={(e) => setNewContent({ ...newContent, description: e.target.value })}
          className="form-input"
        />
        <input
          type="text"
          placeholder="Genre"
          value={newContent.genre}
          onChange={(e) => setNewContent({ ...newContent, genre: e.target.value })}
          className="form-input"
        />
        <input
          type="text"
          placeholder="Rating"
          value={newContent.rating}
          onChange={(e) => setNewContent({ ...newContent, rating: e.target.value })}
          className="form-input"
        />
        <input
          type="text"
          placeholder="Content URL"
          value={newContent.url}
          onChange={(e) => setNewContent({ ...newContent, url: e.target.value })}
          className="form-input"
        />
        <button onClick={editMode ? handleEditContent : handleCreateContent} className="submit-btn">
          {editMode ? "Save Changes" : "Create Content"}
        </button>
      </div>

      <h3 className="content-list-title">Content List</h3>
      <div className="content-lists">
        {contents.map((content) => (
          <div key={content.id} className="content-itemss">
            {content.imageUrl && (
              <img src={content.imageUrl} alt={`${content.title}`} className="content-image" />
            )}
            <div className="content-info">
              <p><strong>{content.title}</strong></p>
              <p>{content.description}</p>
            </div>
            <button className="edit-btn" onClick={() => handleEditClick(content)}>Edit</button>
            <button className="delete-btn" onClick={() => handleDeleteContent(content.id)}>Delete</button>

            <div className="upload-section">
              <input
                type="file"
                onChange={(e) => setSelectedImage(e.target.files[0])}
                className="file-input"
              />
              <button onClick={() => handleImageUpload(content.id)} className="upload-btn">
                Upload Image
              </button>
            </div>

            <div className="upload-section">
              <input
                type="file"
                onChange={(e) => setSelectedFile(e.target.files[0])}
                className="file-input"
              />
              <button onClick={() => handleMediaUpload(content.id)} className="upload-btn">
                Upload Audio/Video
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ContentManagement;
