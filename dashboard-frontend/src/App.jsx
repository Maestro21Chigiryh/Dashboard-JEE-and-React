// src/App.jsx
import React, { useState, useEffect } from 'react';
import Login from './components/Login';
import TopClients from './components/TopClients';
import './App.css';
import './Dashboard.css';

const App = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Vérifier si l'utilisateur est déjà connecté
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error('Error parsing stored user:', e);
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  }, []);

  const handleLogin = (userData) => {
    setUser(userData);
  };

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
  };

  if (loading) {
    return <div className="loading">Loading application...</div>;
  }

  return (
    <div className="app">
      {user ? (
        <div className="dashboard">
          <header className="dashboard-header">
            <h1>Client Dashboard</h1>
            <div className="user-info">
              <span>Welcome, {user.username}</span>
              <button onClick={handleLogout} className="logout-button">Logout</button>
            </div>
          </header>
          <main className="dashboard-content">
            <TopClients />
          </main>
          <footer className="dashboard-footer">
            <p>&copy; 2025 Simple Dashboard App</p>
          </footer>
        </div>
      ) : (
        <Login onLoginSuccess={handleLogin} />
      )}
    </div>
  );
};

export default App;