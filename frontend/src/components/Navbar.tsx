import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { isAuthenticated, isAdmin, username, logout } = useAuth();

  return (
    <nav className="navbar">
      <div className="navbar-inner container">
        <Link className="navbar-brand" to="/">
          <span className="brand-ball"></span> SIW <span className="brand-accent">Tornei</span>
        </Link>
        <div className="navbar-links">
          <Link className="nav-link" to="/tornei/">Tornei</Link>
          <Link className="nav-link" to="/squadre/">Squadre</Link>
          <Link className="nav-link" to="/#tornei">Partite</Link>
          <Link className="nav-link" to="/#tornei">Classifiche</Link>
        </div>
        <div className="navbar-auth">
          {isAuthenticated ? (
            <>
              <span className="nav-user"> {username}</span>
              {isAdmin && <Link className="btn btn-ghost" to="/admin">Area Admin</Link>}
              <button type="button" className="btn btn-accent" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <Link className="btn btn-ghost" to="/login">Login</Link>
              <Link className="btn btn-accent" to="/register">Registrati</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
