import { HeartPulse } from "lucide-react";
import { Link } from "react-router-dom";

export function AppLogo() {
  return (
    <Link
      to="/"
      className="inline-flex items-center gap-2 rounded-md focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
    >
      <span className="grid size-9 place-items-center rounded-md bg-primary text-primary-foreground">
        <HeartPulse className="size-5" />
      </span>
      <span className="text-lg font-bold text-foreground">
        Sangue <span className="text-primary">Amigo</span>
      </span>
    </Link>
  );
}
