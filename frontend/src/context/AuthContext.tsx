import { createContext, useContext, useState, useEffect, type ReactNode } from 'react';

interface AuthContextType {
  isAuthenticated: boolean;
  isAdmin: boolean;
  username: string | null;
  loading: boolean;
  refresh: () => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  isAdmin: false,
  username: null,
  loading: true,
  refresh: async () => {},
  logout: () => {},
});

// Stesso dominio in produzione (Spring serve React); in dev Vite gira su
// un'altra porta, quindi serve l'origin esplicito + withCredentials per
// far viaggiare il cookie JSESSIONID tra :5173 e :8080.
const API_BASE = import.meta.env.VITE_API_BASE ?? '';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);
  const [username, setUsername] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const refresh = async () => {
    try {
      const res = await fetch(`${API_BASE}/api/auth/me`, {
        credentials: 'include', // manda/riceve il cookie JSESSIONID
      });
      if (res.ok) {
        const data = await res.json();
        setIsAuthenticated(true);
        setUsername(data.username);
        setIsAdmin(!!data.isAdmin);
      } else {
        // 401: nessuno loggato, non è un errore applicativo
        setIsAuthenticated(false);
        setUsername(null);
        setIsAdmin(false);
      }
    } catch (err) {
      console.error('Errore nel controllo della sessione:', err);
      setIsAuthenticated(false);
      setUsername(null);
      setIsAdmin(false);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    refresh();
  }, []);

  const logout = () => {
    // Il logout vero lo fa Spring Security su /logout (vedi Navbar):
    // qui ci limitiamo ad azzerare subito lo stato locale per la UI,
    // il browser segue comunque il redirect a /logout lato server.
    setIsAuthenticated(false);
    setUsername(null);
    setIsAdmin(false);
  };

  return (
      <AuthContext.Provider value={{ isAuthenticated, isAdmin, username, loading, refresh, logout }}>
        {children}
      </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);