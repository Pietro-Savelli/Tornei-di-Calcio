import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const API_BASE = import.meta.env.VITE_API_BASE ?? '';

export default function Navbar() {
  const { isAuthenticated, isAdmin, username, logout } = useAuth();
  const [loggingOut, setLoggingOut] = useState(false);

  const handleLogout = async () => {
    setLoggingOut(true);
    try {
      // /logout di Spring Security richiede POST + token CSRF valido
      // (CSRF è disabilitato solo su /api/home, /api/tornei/**, /api/utente/**,
      // /api/auth/**: /logout non è tra questi, quindi serve il token vero).
      const csrfRes = await fetch(`${API_BASE}/api/csrf`, { credentials: 'include' });
      const { headerName, token } = await csrfRes.json();

      await fetch(`${API_BASE}/logout`, {
        method: 'POST',
        credentials: 'include',
        headers: { [headerName]: token },
      });
    } catch (err) {
      console.error('Errore durante il logout:', err);
    } finally {
      logout(); // azzera subito lo stato locale per la UI
      window.location.href = '/'; // ricarica la home come utente anonimo
    }
  };

  return (
      <nav className="navbar">
        <div className="navbar-inner container">
          <Link className="navbar-brand" to="/">
            <span className="brand-ball"></span> SIW <span className="brand-accent">Tornei</span>
          </Link>
          <div className="navbar-links">
            <a className="nav-link" href="http://localhost:8080/tornei/">Tornei</a>
            <a className="nav-link" href="http://localhost:8080/squadre/">Squadre</a>
          </div>
          <div className="navbar-auth">
            {isAuthenticated ? (
                <>
                  {username && <span className="navbar-username">{username}</span>}
                  {isAdmin && (
                      <a className="btn btn-ghost" href="http://localhost:8080/admin/index">
                        Area Admin
                      </a>
                  )}
                  <button className="btn btn-accent" onClick={handleLogout} disabled={loggingOut}>
                    {loggingOut ? '...' : 'Logout'}
                  </button>
                </>
            ) : (
                <>
                  <a className="btn btn-ghost" href="http://localhost:8080/login">Login</a>
                  <a className="btn btn-accent" href="http://localhost:8080/register">Registrati</a>
                </>
            )}
          </div>
        </div>
      </nav>
  );
}