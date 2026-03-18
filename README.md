# Bachat System: Advanced Web UI

Bachat is originally a Core Java CLI Expense Tracker. This project extends the frontend capabilities of Bachat by introducing an ultra-modern, **Cyberpunk/Sci-Fi themed Web UI**. 

This system acts as a highly aesthetic presentation layer that leverages your preexisting persistence setup (`expenses.csv`, `budget.txt`, `user.txt`) through a lightweight Express HTTP abstraction layer.

## Key Features

- 🛸 **Cyberpunk Aesthetics**: Features an immersive neon UI (Orbitron & Rajdhani fonts), heavy glassmorphism, animated elements, and distinct glowing borders.
- ⚡ **Zero-Database Integration**: Directly parses and modifies your `expenses.csv` and native text files. Absolutely no database migration required.
- 💳 **Smart Warnings**: Dashboard intelligently tracks Budget Consumption. Panels react dynamically to >80% usage (WARNING) and 100% capacity breaches (CRITICAL).
- 📊 **Dynamic Visualizations**: Uses *Chart.js* injected with neon palettes.
- 🔍 **Live Diagnostics**: Features real-time filtering/searching through the transaction log list.
- ⬇️ **Export.CSV Button**: Instantly dumps the current local storage records directly to the end user's downloads folder.
- 📈 **Highest Spender Metric**: Top stat boxes auto-compute which category yields the biggest drain on credits.

## Setup Instructions

This assumes you already have your `budget.txt`, `user.txt`, and `expenses.csv` in the root folder alongside your original Java environment.

1. Ensure **Node.js** (v14+) is installed.
2. Open your terminal in this repository.
3. Install dependencies:
   ```bash
   npm install express cors body-parser
   ```
4. Start the backend server:
   ```bash
   node server.js
   ```
5. Open your web browser and navigate to:
   ```
   http://localhost:3000
   ```

## Technologies & Stack
- **Frontend**: HTML5, Vanilla CSS3 (CSS Variables, Flex/Grid Layouts, Keyframe animations), JS Fetch API.
- **Backend**: Node.js, Express.js (Native `fs` stream reader module).
- **Libraries**: Chart.js (Pulled from CDN).

## Future Implementations
- Authentication layer wrapper.
- PWA standard compliance for offline mobile access.

---
*Created as part of the Bachat application upgrade cycle.*
