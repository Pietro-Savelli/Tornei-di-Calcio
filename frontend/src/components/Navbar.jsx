import React from 'react';
import { APP } from '../config/appConfig';

function Navbar() {
  return (
    <nav className="navbar">
      <div className="navbar-inner container">
        <a className="navbar-brand" href="/">
          <span className="brand-ball"></span> SIW <span className="brand-accent">Tornei</span>
        </a>
        <div className="navbar-links">
          <a className="nav-link" href="/tornei/">Tornei</a>
          <a className="nav-link" href="/squadre/">Squadre</a>
          <a className="nav-link" href="#tornei">Partite</a>
          <a className="nav-link" href="#tornei">Classifiche</a>
        </div>
        <div className="navbar-auth">
          {APP.authenticated ? (
            <>
              <span className="nav-user"> {APP.username}</span>
              {APP.isAdmin && <a className="btn btn-ghost" href="/admin">Area Admin</a>}
              <form action="/logout" method="post" className="logout-form">
                <input type="hidden" name={APP.csrf.param} value={APP.csrf.token} />
                <button type="submit" className="btn btn-accent">Logout</button>
              </form>
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

export default Navbar;
