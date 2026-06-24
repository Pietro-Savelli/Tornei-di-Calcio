import { Link } from 'react-router-dom';
import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import api from '../services/api';

export default function Navbar() {
  const { isAuthenticated, isAdmin, username, logout } = useAuth();
  const [loggingOut, setLoggingOut] = useState(false);

  const handleLogout = async () => {
    setLoggingOut(true);
    try {
      const csrfRes = await api.get('/api/csrf');
      const { headerName, token } = csrfRes.data;

      await api.post('/logout', null, {
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
            <a className="nav-link" href="/tornei/">Tornei</a>
            <a className="nav-link" href="/squadre/">Squadre</a>
          </div>
          <div className="navbar-auth">
            {isAuthenticated ? (
                <>
                  {username && <span className="navbar-username">{username}</span>}
                  {isAdmin && (
                      <a className="btn btn-ghost" href="/admin/index">
                        Area Admin
                      </a>
                  )}
                  <button className="btn btn-accent" onClick={handleLogout} disabled={loggingOut}>
                    {loggingOut ? '...' : 'Logout'}
                  </button>
                </>
            ) : (
                <>
                  <a className="btn btn-ghost" href="/login">Login</a>
                  <a className="btn btn-accent" href="/register">Registrati</a>
                </>
            )}
          </div>
        </div>
      </nav>
  );
}