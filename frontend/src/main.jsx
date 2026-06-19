import React from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'

// La index.html (Thymeleaf) espone <div id="root"> e window.__APP__ con stato auth + csrf.
const container = document.getElementById('root')
if (container) {
  createRoot(container).render(<App />)
}
