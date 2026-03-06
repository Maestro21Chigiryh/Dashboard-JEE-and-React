import React, { useState, useEffect } from 'react';
import { getTopClientsTransactions } from '../services/api';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const TransactionBarChart = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const response = await getTopClientsTransactions(5);
        
        if (response.clients && Array.isArray(response.clients)) {
          // Format data for the chart
          const chartData = response.clients.map(client => ({
            name: client.name.split(' ')[0], // Use first name for better display
            purchaseAmount: client.purchaseAmount,
            purchaseCount: client.purchaseCount
          }));
          
          setData(chartData);
        }
      } catch (err) {
        console.error("Error fetching chart data:", err);
        setError("Failed to load chart data");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <div className="chart-loading">Loading chart data...</div>;
  if (error) return <div className="chart-error">{error}</div>;
  if (data.length === 0) return <div className="no-chart-data">No data available for chart</div>;

  return (
    <div className="chart-container">
      <h3>Top Clients Overview</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data} margin={{ top: 20, right: 30, left: 20, bottom: 5 }}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis yAxisId="left" orientation="left" stroke="#8884d8" />
          <YAxis yAxisId="right" orientation="right" stroke="#82ca9d" />
          <Tooltip formatter={(value, name) => {
            if (name === "purchaseAmount") return [`€${value.toFixed(2)}`, "Purchase Amount"];
            return [value, "Purchase Count"];
          }} />
          <Legend />
          <Bar yAxisId="left" dataKey="purchaseAmount" name="Purchase Amount (€)" fill="#8884d8" />
          <Bar yAxisId="right" dataKey="purchaseCount" name="Purchase Count" fill="#82ca9d" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default TransactionBarChart;