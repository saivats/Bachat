// State
let expenses = [];
let budget = 0;
let userProfile = { name: '', city: '' };
let chartInstance = null;

// Initial Load
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('expDate').valueAsDate = new Date();
  fetchUser();
  fetchBudget();
  fetchExpenses();
});

// APIs
async function fetchUser() {
  const res = await fetch('/api/user');
  const data = await res.json();
  userProfile = data;
  updateUserUI();
}

async function fetchBudget() {
  const res = await fetch('/api/budget');
  const data = await res.json();
  budget = data.budget;
  updateBudgetUI();
}

async function fetchExpenses() {
  const res = await fetch('/api/expenses');
  expenses = await res.json();
  updateExpensesUI();
}

// UI Updaters
function updateUserUI() {
  document.getElementById('userNameDisplay').innerText = userProfile.name || 'OPERATIVE';
  document.getElementById('userCityDisplay').innerText = userProfile.city || 'SECTOR NULL';
  if(userProfile.name) {
    document.getElementById('userAvatar').innerText = userProfile.name.charAt(0).toUpperCase();
  }
  document.getElementById('inputName').value = userProfile.name;
  document.getElementById('inputCity').value = userProfile.city;
}

function updateBudgetUI() {
  document.getElementById('budgetDisplay').innerText = budget.toFixed(2);
  document.getElementById('inputBudget').value = budget;
  calculateInsights();
}

function updateExpensesUI(filteredList = null) {
  const tbody = document.getElementById('expensesBody');
  const emptyState = document.getElementById('emptyState');
  
  tbody.innerHTML = '';
  const displayList = filteredList || expenses;
  
  if (displayList.length === 0) {
    emptyState.style.display = 'block';
  } else {
    emptyState.style.display = 'none';
    const sorted = [...displayList].reverse();
    
    sorted.forEach(exp => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td style="color: var(--text-dim);">${exp.date}</td>
        <td><span class="tag-label">[${exp.category.toUpperCase()}]</span></td>
        <td>${exp.description}</td>
        <td style="font-weight: 600; color: var(--neon-cyan);">₹${exp.amount.toFixed(2)}</td>
        <td><button class="action-btn" onclick="deleteExpense(${exp.id})">VOID</button></td>
      `;
      tbody.appendChild(tr);
    });
  }
  
  calculateInsights();
}

function filterExpenses() {
  const query = document.getElementById('searchInput').value.toLowerCase();
  const filtered = expenses.filter(e => 
    e.category.toLowerCase().includes(query) || 
    e.description.toLowerCase().includes(query)
  );
  updateExpensesUI(filtered);
}

function calculateInsights() {
  const totalSpent = expenses.reduce((sum, exp) => sum + exp.amount, 0);
  const remaining = budget - totalSpent;
  
  // Highest Category Logic
  const categories = {};
  expenses.forEach(e => {
    categories[e.category] = (categories[e.category] || 0) + e.amount;
  });
  let topCategory = '--';
  let topAmount = 0;
  for (const [cat, amt] of Object.entries(categories)) {
     if(amt > topAmount) { topCategory = cat; topAmount = amt; }
  }
  document.getElementById('topCategoryDisplay').innerText = topCategory;

  // Warnings
  const warningBox = document.getElementById('warningBox');
  const warningText = document.getElementById('warningText');
  const progressDiv = document.getElementById('budgetProgress');
  
  let percentage = budget > 0 ? (totalSpent / budget) * 100 : 0;
  
  document.getElementById('totalSpentDisplay').innerText = `₹${totalSpent.toFixed(2)}`;
  document.getElementById('remainingDisplay').innerText = `₹${remaining.toFixed(2)}`;
  document.getElementById('budgetStatusText').innerText = `${percentage.toFixed(1)}% OVERLAY`;
  progressDiv.style.width = `${Math.min(percentage, 100)}%`;
  
  if (percentage >= 100) {
    warningBox.style.display = 'flex';
    warningBox.className = 'stat-box cyber-panel glow-border danger-box';
    warningText.className = 'alert-text';
    warningText.innerText = 'CAPACITY BREACH';
    progressDiv.style.background = 'var(--neon-pink)';
    progressDiv.style.boxShadow = '0 0 10px var(--neon-pink)';
  } else if (percentage >= 80) {
    warningBox.style.display = 'flex';
    warningBox.className = 'stat-box cyber-panel glow-border warning-box';
    warningText.className = 'alert-text';
    warningText.innerText = 'WARNING: 80% LOAD';
    progressDiv.style.background = 'var(--neon-yellow)';
    progressDiv.style.boxShadow = '0 0 10px var(--neon-yellow)';
  } else {
    warningBox.style.display = 'none';
    progressDiv.style.background = 'var(--neon-green)';
    progressDiv.style.boxShadow = '0 0 10px var(--neon-green)';
  }
  
  updateChart(categories);
}

// Chart.js Setup with Cyberpunk Colors
Chart.defaults.color = '#7dd3fc';
Chart.defaults.font.family = "'Rajdhani', sans-serif";
Chart.defaults.font.size = 14;

function updateChart(categories) {
  const ctx = document.getElementById('categoryChart').getContext('2d');
  const labels = Object.keys(categories);
  const data = Object.values(categories);
  
  if (chartInstance) chartInstance.destroy();
  
  chartInstance = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: labels,
      datasets: [{
        data: data,
        backgroundColor: ['#00f3ff', '#bc13fe', '#00ff66', '#fcee0a', '#ff003c', '#e0f2fe'],
        borderColor: '#050a10',
        borderWidth: 2,
        hoverOffset: 10
      }]
    },
    options: {
      responsive: true, maintainAspectRatio: false,
      plugins: {
        legend: { position: 'right', labels: { padding: 20 } }
      },
      cutout: '75%'
    }
  });
}

function exportCSV() {
  if (expenses.length === 0) return alert('No data to export.');
  let csvContent = "data:text/csv;charset=utf-8,ID,Amount,Category,Date,Description\n";
  expenses.forEach(e => {
    csvContent += `${e.id},${e.amount},${e.category},${e.date},${e.description}\n`;
  });
  const encodedUri = encodeURI(csvContent);
  const link = document.createElement("a");
  link.setAttribute("href", encodedUri);
  link.setAttribute("download", `Bachat_System_Export_${new Date().getTime()}.csv`);
  document.body.appendChild(link); // Required for FF
  link.click();
  link.remove();
}

// Actions
document.getElementById('profileForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const name = document.getElementById('inputName').value;
  const city = document.getElementById('inputCity').value;
  await fetch('/api/user', {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ name, city })
  });
  fetchUser();
  closeModals();
});

document.getElementById('budgetForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const amt = parseFloat(document.getElementById('inputBudget').value);
  await fetch('/api/budget', {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ budget: amt })
  });
  fetchBudget();
  closeModals();
});

document.getElementById('expenseForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const amount = parseFloat(document.getElementById('expAmount').value);
  const category = document.getElementById('expCategory').value;
  const date = document.getElementById('expDate').value;
  const description = document.getElementById('expDesc').value;
  
  await fetch('/api/expenses', {
    method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ amount, category, date, description })
  });
  
  fetchExpenses();
  closeModals();
  document.getElementById('expenseForm').reset();
  document.getElementById('expDate').valueAsDate = new Date();
});

async function deleteExpense(id) {
  if(confirm('Warning: Destructive action. Void this log?')) {
    await fetch(`/api/expenses/${id}`, { method: 'DELETE' });
    fetchExpenses();
  }
}

// Modal Mngmt
function openProfileModal() { document.getElementById('profileModal').style.display = 'flex'; }
function openBudgetModal() { document.getElementById('budgetModal').style.display = 'flex'; }
function openExpenseModal() { document.getElementById('expenseModal').style.display = 'flex'; }
function closeModals() { document.querySelectorAll('.modal').forEach(m => m.style.display = 'none'); }

window.onclick = function(event) { if (event.target.classList.contains('modal')) closeModals(); }
