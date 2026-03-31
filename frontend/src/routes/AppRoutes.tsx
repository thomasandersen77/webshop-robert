import { Navigate, Route, Routes } from 'react-router-dom';
import { AppShell } from '../components/layout/AppShell';
import DesignA from '../pages/DesignA';
import DesignB from '../pages/DesignB';
import DesignC from '../pages/DesignC';

export function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/design-a" replace />} />
      <Route element={<AppShell />}>
        <Route path="design-a" element={<DesignA />} />
        <Route path="design-b" element={<DesignB />} />
        <Route path="design-c" element={<DesignC />} />
      </Route>
      <Route path="*" element={<Navigate to="/design-a" replace />} />
    </Routes>
  );
}
