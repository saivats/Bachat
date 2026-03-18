const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));

const DATA_DIR = __dirname;
const BUDGET_FILE = path.join(DATA_DIR, 'budget.txt');
const USER_FILE = path.join(DATA_DIR, 'user.txt');
const EXPENSES_FILE = path.join(DATA_DIR, 'expenses.csv');

// Helpers
const readFileSafe = (filePath, defaultVal = '') => {
  try {
    return fs.existsSync(filePath) ? fs.readFileSync(filePath, 'utf-8') : defaultVal;
  } catch (err) { return defaultVal; }
};

const writeFileSafe = (filePath, content) => {
  fs.writeFileSync(filePath, content, 'utf-8');
};

// API: Budget
app.get('/api/budget', (req, res) => {
  const content = readFileSafe(BUDGET_FILE, '0.0');
  res.json({ budget: parseFloat(content.trim()) || 0 });
});

app.post('/api/budget', (req, res) => {
  const { budget } = req.body;
  if(budget !== undefined) {
    writeFileSafe(BUDGET_FILE, String(budget));
    res.json({ success: true, budget });
  } else {
    res.status(400).json({ error: 'Budget missing' });
  }
});

// API: User
app.get('/api/user', (req, res) => {
  const content = readFileSafe(USER_FILE, '\n').split('\n');
  const name = content[0]?.trim() || '';
  const city = content[1]?.trim() || '';
  res.json({ name, city });
});

app.post('/api/user', (req, res) => {
  const { name, city } = req.body;
  if(name !== undefined && city !== undefined) {
    writeFileSafe(USER_FILE, `${name}\n${city}\n`);
    res.json({ success: true, name, city });
  } else {
    res.status(400).json({ error: 'User data missing' });
  }
});

// API: Expenses
app.get('/api/expenses', (req, res) => {
  const content = readFileSafe(EXPENSES_FILE, '').trim();
  if (!content) return res.json([]);
  
  const lines = content.split('\n');
  const expenses = lines.map(line => {
    const [id, amount, category, date, ...descArray] = line.split(',');
    return {
      id: parseInt(id),
      amount: parseFloat(amount),
      category: category,
      date: date,
      description: descArray.join(',').trim()
    };
  }).filter(e => !isNaN(e.id));
  
  res.json(expenses);
});

app.post('/api/expenses', (req, res) => {
  const { amount, category, date, description } = req.body;
  if(amount === undefined || !category || !date) {
    return res.status(400).json({ error: 'Missing expense fields' });
  }

  const content = readFileSafe(EXPENSES_FILE, '').trim();
  const lines = content ? content.split('\n') : [];
  let nextId = 1;
  if (lines.length > 0) {
    const lastLine = lines[lines.length - 1];
    const lastId = parseInt(lastLine.split(',')[0]);
    if (!isNaN(lastId)) nextId = lastId + 1;
  }

  const newCsvLine = `${nextId},${amount},${category},${date},${description || ''}\n`;
  if (content === '') {
     writeFileSafe(EXPENSES_FILE, newCsvLine);
  } else {
     fs.appendFileSync(EXPENSES_FILE, newCsvLine.startsWith('\n') ? newCsvLine : '\n' + newCsvLine);
     // Clean up multiple newlines that might occur
     const cleaned = readFileSafe(EXPENSES_FILE).split('\n').filter(l => l.trim()).join('\n') + '\n';
     writeFileSafe(EXPENSES_FILE, cleaned);
  }
  
  res.json({ success: true, id: nextId });
});

app.delete('/api/expenses/:id', (req, res) => {
  const idToDelete = parseInt(req.params.id);
  const content = readFileSafe(EXPENSES_FILE, '').trim();
  if(!content) return res.json({ success: true });

  const lines = content.split('\n');
  const filtered = lines.filter(line => {
    const parts = line.split(',');
    return parseInt(parts[0]) !== idToDelete;
  });

  writeFileSafe(EXPENSES_FILE, filtered.join('\n') + (filtered.length ? '\n' : ''));
  res.json({ success: true });
});

app.listen(PORT, () => {
  console.log(`Bachat Web UI Server running on http://localhost:${PORT}`);
});
