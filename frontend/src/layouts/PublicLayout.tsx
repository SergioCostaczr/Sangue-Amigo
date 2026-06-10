import { LogIn, Menu, UserRoundPlus, X } from "lucide-react";
import { useState } from "react";
import { Link, NavLink, Outlet } from "react-router-dom";
import { AppLogo } from "@/components/AppLogo";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";

const links = [
  { to: "/", label: "Inicio", end: true },
  { to: "/hemocentros", label: "Hemocentros" },
  { to: "/campanhas", label: "Campanhas" },
];

export function PublicLayout() {
  const [open, setOpen] = useState(false);

  return (
    <div className="min-h-screen bg-background">
      <header className="sticky top-0 z-40 border-b border-border bg-background/95 backdrop-blur">
        <div className="page-container flex h-16 items-center justify-between">
          <AppLogo />

          <nav className="hidden items-center gap-1 md:flex" aria-label="Navegacao principal">
            {links.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                end={link.end}
                className={({ isActive }) =>
                  cn(
                    "rounded-md px-3 py-2 text-sm font-medium transition",
                    isActive ? "bg-accent text-accent-foreground" : "text-muted-foreground hover:text-foreground",
                  )
                }
              >
                {link.label}
              </NavLink>
            ))}
          </nav>

          <div className="hidden items-center gap-2 md:flex">
            <Link to="/login"><Button variant="ghost"><LogIn className="size-4" /> Entrar</Button></Link>
            <Link to="/cadastro"><Button><UserRoundPlus className="size-4" /> Criar conta</Button></Link>
          </div>

          <button
            type="button"
            className="rounded-md p-2 text-muted-foreground hover:bg-muted md:hidden"
            onClick={() => setOpen((value) => !value)}
            aria-label={open ? "Fechar menu" : "Abrir menu"}
          >
            {open ? <X className="size-5" /> : <Menu className="size-5" />}
          </button>
        </div>

        {open && (
          <div className="border-t border-border bg-background px-4 py-4 md:hidden">
            <nav className="flex flex-col gap-1">
              {links.map((link) => (
                <NavLink
                  key={link.to}
                  to={link.to}
                  end={link.end}
                  onClick={() => setOpen(false)}
                  className="rounded-md px-3 py-2 text-sm font-medium text-muted-foreground hover:bg-muted hover:text-foreground"
                >
                  {link.label}
                </NavLink>
              ))}
              <NavLink to="/login" onClick={() => setOpen(false)} className="rounded-md px-3 py-2 text-sm font-medium text-muted-foreground hover:bg-muted">
                Entrar
              </NavLink>
              <NavLink to="/cadastro" onClick={() => setOpen(false)} className="rounded-md px-3 py-2 text-sm font-medium text-primary">
                Criar conta
              </NavLink>
            </nav>
          </div>
        )}
      </header>

      <main><Outlet /></main>

      <footer className="border-t border-border bg-card">
        <div className="page-container flex flex-col gap-3 py-8 text-sm text-muted-foreground sm:flex-row sm:items-center sm:justify-between">
          <AppLogo />
          <p>Conectando doadores e hemocentros.</p>
        </div>
      </footer>
    </div>
  );
}
