/* ===================== Variables globales ===================== */
:root {
  --primary-color: #1a3a5f;
  --secondary-color: #2980b9;
  --accent-color: #e74c3c;
  --background-color: #f8f9fa;
  --text-color: #2c3e50;
  --border-color: #d1d9e6;
  --success-color: #27ae60;
  --warning-color: #f39c12;
  --shadow-soft: 0 5px 15px rgba(0, 0, 0, 0.05);
  --shadow-strong: 0 8px 30px rgba(0, 0, 0, 0.12);
  --gradient-primary: linear-gradient(135deg, #1a3a5f 0%, #2c5282 100%);
  --gradient-secondary: linear-gradient(135deg, #2980b9 0%, #3498db 100%);
  --font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* ===================== Global styles ===================== */
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: var(--font-family);
  background-color: var(--background-color);
  color: var(--text-color);
  line-height: 1.6;
  min-height: 100vh;
}

a {
  color: var(--secondary-color);
  text-decoration: none;
  font-weight: 500;
}

a:hover {
  color: var(--primary-color);
}

/* ===================== Buttons ===================== */
button {
  border-radius: 8px;
  border: 1px solid transparent;
  padding: 0.6em 1.2em;
  font-size: 1em;
  font-weight: 500;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.3s ease;
}

button:focus,
button:focus-visible {
  outline: 4px auto -webkit-focus-ring-color;
}

.primary-button {
  background: var(--gradient-secondary);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
  box-shadow: 0 4px 6px rgba(41, 128, 185, 0.15);
}

.primary-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(41, 128, 185, 0.2);
}

.primary-button:disabled {
  background: linear-gradient(135deg, #a9c7ed 0%, #c4d8ee 100%);
  cursor: not-allowed;
  box-shadow: none;
}

.danger-button {
  background: linear-gradient(135deg, #e74c3c 0%, #ef5350 100%);
  color: white;
  border: none;
  padding: 10px 18px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
  box-shadow: 0 4px 6px rgba(231, 76, 60, 0.15);
}

.danger-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(231, 76, 60, 0.2);
}

.danger-button:disabled {
  background: linear-gradient(135deg, #eaaeae 0%, #f5c4c4 100%);
  cursor: not-allowed;
  box-shadow: none;
}

/* ===================== Loading Spinner ===================== */
.loading-screen {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: var(--background-color);
  color: var(--primary-color);
}

.loading-spinner {
  width: 60px;
  height: 60px;
  border: 6px solid rgba(41, 128, 185, 0.15);
  border-top-color: var(--secondary-color);
  border-radius: 50%;
  animation: spin 1s cubic-bezier(0.45, 0, 0.55, 1) infinite;
  margin-bottom: 25px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ===================== Login ===================== */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 20px;
  background: var(--gradient-primary);
}

.login-box {
  background-color: white;
  border-radius: 12px;
  box-shadow: var(--shadow-strong);
  padding: 40px;
  width: 100%;
  max-width: 420px;
  position: relative;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.login-box h2 {
  margin-bottom: 25px;
  color: var(--primary-color);
  text-align: center;
  font-size: 28px;
  font-weight: 700;
}

.form-group {
  margin-bottom: 25px;
  position: relative;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  font-size: 14px;
  color: #546e7a;
}

.form-group input {
  width: 100%;
  padding: 14px 16px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s;
  background-color: #f8fafc;
}

.form-group input:focus {
  border-color: var(--secondary-color);
  box-shadow: 0 0 0 4px rgba(41, 128, 185, 0.1);
  outline: none;
  background-color: white;
}

/* ===================== Dashboard ===================== */
.dashboard-container {
  padding: 30px;
  max-width: 1300px;
  margin: 0 auto;
  width: 100%;
}

.dashboard-header {
  background: var(--gradient-primary);
  color: white;
  padding: 18px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: var(--shadow-soft);
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
  gap: 30px;
  margin-top: 30px;
}

.top-clients-container {
  background-color: white;
  border-radius: 12px;
  box-shadow: var(--shadow-soft);
  padding: 25px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.top-clients-container:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-strong);
}

.top-clients-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--border-color);
}

.top-clients-table {
  overflow-x: auto;
  border-radius: 8px;
  box-shadow: 0 0 0 1px var(--border-color);
}

.top-clients-table table {
  width: 100%;
  border-collapse: separate;
}

.top-clients-table th, 
.top-clients-table td {
  padding: 16px;
  text-align: left;
}

.top-clients-table th {
  background-color: #f8fafc;
  font-weight: 600;
  color: #64748b;
  text-transform: uppercase;
  font-size: 13px;
  letter-spacing: 0.5px;
}

.top-clients-table tr:hover {
  background-color: #f1f5f9;
}

.no-data {
  text-align: center;
  padding: 50px !important;
  color: #64748b;
  font-style: italic;
  background-color: #f8fafc;
  border-radius: 8px;
}

/* ===================== Responsive ===================== */
@media (max-width: 768px) {
  .dashboard-header { flex-direction: column; gap: 15px; }
  .dashboard-grid { grid-template-columns: 1fr; }
  .top-clients-header { flex-direction: column; align-items: flex-start; gap: 15px; }
  .client-stats, .button-group { width: 100%; flex-direction: column; }
  .primary-button, .danger-button { flex: 1; }
  .login-box { padding: 30px 20px; }
}

@media (min-width: 1400px) {
  .dashboard-grid { grid-template-columns: repeat(auto-fit, minmax(400px, 1fr)); gap: 40px; }
  .dashboard-container { padding: 40px; }
}

/* ===================== Animations ===================== */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.top-clients-container {
  animation: fadeIn 0.4s ease-out;
}

.dashboard-grid > * {
  animation: fadeIn 0.4s ease-out;
}

.dashboard-grid > *:nth-child(2) { animation-delay: 0.1s; }
.dashboard-grid > *:nth-child(3) { animation-delay: 0.2s; }
.dashboard-grid > *:nth-child(4) { animation-delay: 0.3s; }

/* ===================== Scrollbar ===================== */
::-webkit-scrollbar { width: 12px; height: 12px; }
::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 10px; }
::-webkit-scrollbar-thumb { background: #c5d0dc; border-radius: 10px; border: 3px solid #f1f1f1; }
::-webkit-scrollbar-thumb:hover { background: #a9b7c6; }