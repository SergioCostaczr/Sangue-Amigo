import { cn } from "@/lib/utils";

const styles: Record<string, string> = {
  PENDENTE: "bg-amber-100 text-amber-800",
  CONFIRMADO: "bg-blue-100 text-blue-800",
  CANCELADO: "bg-muted text-muted-foreground",
  CONCLUIDO: "bg-emerald-100 text-emerald-800",
  AGENDADA: "bg-blue-100 text-blue-800",
  ATIVA: "bg-emerald-100 text-emerald-800",
  ENCERRADA: "bg-muted text-muted-foreground",
};

export function StatusBadge({ status }: { status: string }) {
  return (
    <span className={cn("inline-flex rounded-full px-2.5 py-1 text-xs font-semibold", styles[status] || styles.CANCELADO)}>
      {status}
    </span>
  );
}
