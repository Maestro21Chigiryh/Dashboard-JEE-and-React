import React, { useState, useEffect } from 'react';
import { getClientTransactions } from '../services/api';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';


const ClientTransactionDetails = ({ clientId, clientName }) => {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState({
    total: 0,
    average: 0,
    highest: 0,
    lowest: 0,
    count: 0
  });

// Dans ClientTransactionDetails.jsx
// Modifiez le hook useEffect

useEffect(() => {
  const fetchTransactions = async () => {
    if (!clientId) {
      setError("No client ID provided");
      setLoading(false);
      return;
    }
    
    try {
      setLoading(true);
      setError(null);
      
      console.log("Fetching transactions with client ID:", clientId);
      
      const data = await getClientTransactions(clientId);
      
      if (Array.isArray(data) && data.length > 0) {
        // Sort transactions by date
        const sortedTransactions = [...data].sort((a, b) => 
          new Date(a.date) - new Date(b.date)
        );
        
        // Calculate statistics
        const amounts = sortedTransactions.map(t => t.amount);
        const total = amounts.reduce((sum, amount) => sum + amount, 0);
        const average = total / amounts.length;
        const highest = Math.max(...amounts);
        const lowest = Math.min(...amounts);
        
        setTransactions(sortedTransactions);
        setStats({
          total: total.toFixed(2),
          average: average.toFixed(2),
          highest: highest.toFixed(2),
          lowest: lowest.toFixed(2),
          count: sortedTransactions.length
        });
      } else {
        setTransactions([]);
        setError("No transactions found for this client");
      }
    } catch (err) {
      console.error("Error fetching client transactions:", err);
      setError(`Failed to load transactions: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  fetchTransactions();
}, [clientId]);

  const formatChartData = (transactions) => {
    return transactions.map(t => ({
      date: t.date,
      amount: t.amount,
      product: t.productName,
      quantity: t.quantity
    }));
  };

  if (loading) return <div className="loading">Loading transaction details...</div>;
  
  return (
    <div className="client-transactions">
      <h3>Transaction History - {clientName}</h3>
      
      {error && <div className="error-message">{error}</div>}
      
      {!error && transactions.length > 0 && (
        <>
          <div className="transaction-stats">
            <div className="stat-card">
              <div className="stat-label">Total Spent</div>
              <div className="stat-value">€{stats.total}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Average Purchase</div>
              <div className="stat-value">€{stats.average}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Highest Purchase</div>
              <div className="stat-value">€{stats.highest}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Lowest Purchase</div>
              <div className="stat-value">€{stats.lowest}</div>
            </div>
            <div className="stat-card">
              <div className="stat-label">Transaction Count</div>
              <div className="stat-value">{stats.count}</div>
            </div>
          </div>
          
          <div className="transaction-chart">
            <h4>Purchase History Trend</h4>
            <ResponsiveContainer width="100%" height={300}>
              <LineChart
                data={formatChartData(transactions)}
                margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
              >
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip 
                  formatter={(value, name) => {
                    if (name === "amount") return [`€${value.toFixed(2)}`, "Amount"];
                    return [value, name.charAt(0).toUpperCase() + name.slice(1)];
                  }}
                />
                <Legend />
                <Line type="monotone" dataKey="amount" stroke="#8884d8" activeDot={{ r: 8 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
          
          <div className="transaction-list">
            <h4>Recent Transactions</h4>
            <table>
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Product</th>
                  <th>Quantity</th>
                  <th>Amount</th>
                </tr>
              </thead>
              <tbody>
                {transactions.slice(0, 10).map((transaction, index) => (
                  <tr key={`transaction-${index}`}>
                    <td>{transaction.date}</td>
                    <td>{transaction.productName}</td>
                    <td>{transaction.quantity}</td>
                    <td>€{transaction.amount.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
      
      {!error && transactions.length === 0 && (
        <div className="no-transactions">
          No transactions found for this client
        </div>
      )}
    </div>
  );
};

export default ClientTransactionDetails;