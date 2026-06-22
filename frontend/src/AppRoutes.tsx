import { Routes, Route } from 'react-router-dom';
import HomePage from './pages/HomePage';
import PrivateRoute from './components/PrivateRoute';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<div>Login Page (da implementare)</div>} />
      <Route path="/register" element={<div>Register Page (da implementare)</div>} />
      <Route element={<PrivateRoute />}>
        <Route path="/admin" element={<div>Admin Page (da implementare)</div>} />
      </Route>
      <Route path="*" element={<div>404 Not Found</div>} />
    </Routes>
  );
}
