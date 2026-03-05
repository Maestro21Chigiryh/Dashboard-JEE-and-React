// Dans TopClients.jsx
import React, { useEffect, useState } from 'react';
import { getTopClients, generateClients, getClientCount, resetClients } from '../services/api';
import TransactionBarChart from './TransactionBarChart';

const TopClients = () => {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [clientCount, setClientCount] = useState(0);
  const [generating, setGenerating] = useState(false);
  const [resetting, setResetting] = useState(false);
  const [selectedClient, setSelectedClient] = useState(null);
  const [progress, setProgress] = useState(0);

  // Fonction pour générer un ID MongoDB à partir d'un timestamp
  const generateIdFromTimestamp = (timestamp) => {
    // Convertir le timestamp en hexadécimal et le remplir pour atteindre 24 caractères
    const hexTimestamp = timestamp.toString(16);
    // Padding pour avoir 24 caractères au total
    return hexTimestamp.padStart(24, '0');
  };

  const fetchTopClients = async () => {
    try {
      setLoading(true);
      const data = await getTopClients(5);
      
      console.log("Raw client data from API:", data);
      
      // Utiliser directement les données de l'API sans reformatage des IDs
      setClients(data);
      
      const count = await getClientCount();
      setClientCount(count);
      setError(null);
    } catch (err) {
      setError('Failed to fetch clients');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  
  // Le reste de votre code reste inchangé
  
  const handleGenerateClients = async () => {
    try {
      setGenerating(true);
      setProgress(0);
      
      const progressInterval = setInterval(() => {
        setProgress(prev => {
          const newProgress = prev + 5;
          return newProgress > 95 ? 95 : newProgress;
        });
      }, 500);
      
      await generateClients(10000);
      
      clearInterval(progressInterval);
      setProgress(100);
      fetchTopClients();
      
      setTimeout(() => setProgress(0), 1000);
    } catch (err) {
      setError('Failed to generate clients');
      console.error(err);
    } finally {
      setGenerating(false);
    }
  };

  const handleResetClients = async () => {
    try {
      setResetting(true);
      await resetClients();
      setClients([]);
      setClientCount(0);
      setSelectedClient(null);
    } catch (err) {
      setError('Failed to reset clients');
      console.error(err);
    } finally {
      setResetting(false);
    }
  };

  useEffect(() => {
    fetchTopClients();
  }, []);

  if (loading && !generating && !resetting) {
    return <div>Loading top clients...</div>;
  }

  return (
    <div className="dashboard-container">
      <div className="top-clients-header">
        <h2>Client Dashboard</h2>
        <div className="client-stats">
          <span>Total Clients: {clientCount}</span>
          <div className="button-group">
            <button 
              onClick={handleGenerateClients} 
              disabled={generating || clientCount > 0}
              className="primary-button"
            >
              {generating ? 'Generating...' : 'Generate 10,000 Clients'}
            </button>
            <button 
              onClick={handleResetClients} 
              disabled={resetting || clientCount === 0}
              className="danger-button"
            >
              {resetting ? 'Resetting...' : 'Reset Clients'}
            </button>
          </div>
        </div>
      </div>
      
      {generating && (
        <div className="progress-container">
          <div className="progress-bar" style={{ width: `${progress}%` }}></div>
          <div className="progress-text">{progress}% - Generating clients and transactions...</div>
        </div>
      )}
      
      {error && <div className="error-message">{error}</div>}
      
      <div className="dashboard-grid">
        <div className="top-clients-section">
          <h3>Top 5 Clients</h3>
          <div className="top-clients-table">
            <table>
              <thead>
                <tr>
                  <th>Rank</th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Purchase Amount</th>
                  <th>Purchase Count</th>
                  <th>Last Purchase</th>
                </tr>
              </thead>
              <tbody>

              {clients.length > 0 ? (
                clients.map((client, index) => {
                  // Générer une clé unique pour chaque ligne
                  const uniqueKey = `client-${index}-${client.name?.replace(/\s+/g, '') || 'unknown'}`;
                  
                  return (
                    <tr key={uniqueKey} className={selectedClient && selectedClient.id === client.id ? 'selected' : ''}>
                      <td>{index + 1}</td>
                      <td>{client.name}</td>
                      <td>{client.email}</td>
                      <td>€{client.purchaseAmount.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
                      <td>{client.purchaseCount}</td>
                      <td>{client.lastPurchaseDate}</td>
                    </tr>
                  );
                })
              ) : (
                <tr>
                  <td colSpan="7" className="no-data">
                    No clients found. {clientCount === 0 && 'Click "Generate Clients" to create sample data.'}
                  </td>
                </tr>
              )}
              </tbody>
            </table>
          </div>
        </div>
        
        <div className="charts-section">
          {clients.length > 0 && (
            <div className="bar-chart-container">
              <TransactionBarChart />
            </div>
          )}
        </div>
        
      </div>
    </div>
  );
};

export default TopClients;