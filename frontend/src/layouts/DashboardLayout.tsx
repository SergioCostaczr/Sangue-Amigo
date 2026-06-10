import {
  CalendarDays,
  HeartHandshake,
  History,
  LayoutDashboard,
  LogOut,
  Megaphone,
  QrCode,
  UserRound,
  type LucideIcon,
} from "lucide-react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { AppLogo } from "@/components/AppLogo";
import { useAuth } from "@/features/auth/use-auth";
import { cn } from "@/lib/utils";

interface NavigationItem {
  to: string;
  label: string;
  icon: LucideIcon;
}

const usuarioItems: NavigationItem[] = [
  { to: "/usuario/agendamentos", label: "Agendamentos", icon: CalendarDays },
  { to: "/usuario/agendar", label: "Agendar doacao", icon: HeartHandshake },
  { to: "/usuario/doacoes", label: "Historico", icon: History },
  { to: "/usuario/perfil", label: "Perfil", icon: UserRound },
];

const hemocentroItems: NavigationItem[] = [
  { to: "/hemocentro/painel", label: "Painel", icon: LayoutDashboard },
  { to: "/hemocentro/horarios", label: "Horarios", icon: CalendarDays },
  { to: "/hemocentro/campanhas", label: "Campanhas", icon: Megaphone },
  { to: "/hemocentro/validar-qrcode", label: "Validar QR Code", icon: QrCode },
  { to: "/hemocentro/doacoes", label: "Doacoes", icon: History },
  { to: "/hemocentro/perfil", label: "Perfil", icon: UserRound },
];

export function DashboardLayout({ mode }: { mode: "usuario" | "hemocentro" }) {
  const items = mode === "usuario" ? usuarioItems : hemocentroItems;
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/", { replace: true });
  };

  return (
    <div className="min-h-screen bg-background lg:grid lg:grid-cols-[240px_1fr]">
      <aside className="border-b border-border bg-card lg:sticky lg:top-0 lg:h-screen lg:border-r lg:border-b-0">
        <div className="flex h-16 items-center justify-between px-4 lg:px-5">
          <AppLogo />
          <button
            type="button"
            onClick={handleLogout}
            className="rounded-md p-2 text-muted-foreground hover:bg-muted hover:text-foreground lg:hidden"
            aria-label="Sair"
          >
            <LogOut className="size-5" />
          </button>
        </div>

        <nav className="flex gap-1 overflow-x-auto px-3 pb-3 lg:flex-col lg:overflow-visible">
          {items.map(({ to, label, icon: Icon }) => (
            <NavLink
              key={to}
              to={to}
              className={({ isActive }) =>
                cn(
                  "flex shrink-0 items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium transition",
                  isActive
                    ? "bg-accent text-accent-foreground"
                    : "text-muted-foreground hover:bg-muted hover:text-foreground",
                )
              }
            >
              <Icon className="size-4" />
              {label}
            </NavLink>
          ))}
        </nav>

        <button
          type="button"
          onClick={handleLogout}
          className="mx-3 mt-2 hidden w-[calc(100%-1.5rem)] items-center gap-3 rounded-md px-3 py-2.5 text-sm font-medium text-muted-foreground hover:bg-muted hover:text-foreground lg:flex"
        >
          <LogOut className="size-4" />
          Sair
        </button>
      </aside>

      <main className="min-w-0">
        <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8 lg:py-8">
          <Outlet />
        </div>
      </main>
    </div>
  );
}
