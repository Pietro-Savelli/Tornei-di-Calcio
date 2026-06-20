import React from 'react'
import { createRoot } from 'react-dom/client'
import HomePage from './pages/HomePage.jsx'
import './index.css'

const container = document.getElementById('root')
if (container) {
  createRoot(container).render(<HomePage />)
}
