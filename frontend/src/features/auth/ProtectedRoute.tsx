import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAuth } from "@/features/auth/use-auth";
import { roleHome } from "@/lib/navigation";
import type { Role } from "@/types/auth";

export function ProtectedRoute({ allowedRole }: { allowedRole: Role }) {
  const { authenticated, role } = useAuth();
  const location = useLocation();

  if (!authenticated) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  }

  if (role !== allowedRole) {
    return <Navigate to={roleHome(role)} replace />;
  }

  return <Outlet />;
}

export function PublicOnlyRoute() {
  const { authenticated, role } = useAuth();

  if (authenticated) {
    return <Navigate to={roleHome(role)} replace />;
  }

  return <Outlet />;
}
