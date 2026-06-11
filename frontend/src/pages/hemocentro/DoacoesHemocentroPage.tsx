import { useQuery } from "@tanstack/react-query";
import { CalendarDays, ClipboardList, UserRound } from "lucide-react";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { Card } from "@/components/ui/card";
import { formatDate } from "@/lib/formatters";
import { hemocentroService } from "@/services/hemocentro-service";

export function DoacoesHemocentroPage() {
  const query = useQuery({ queryKey: ["hemocentro", "doacoes"], queryFn: hemocentroService.listarDoacoes });

  return (
    <section>
      <PageHeader title="Historico de doacoes" description="Consulte as doacoes registradas no hemocentro." />
      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar o historico." />}
      {query.data?.length === 0 && <EmptyState title="Nenhuma doacao registrada" description="As doacoes validadas aparecerao aqui." />}
      {query.data && query.data.length > 0 && (
        <Card className="overflow-hidden">
          <div className="hidden grid-cols-[120px_1fr_120px_1fr] gap-4 border-b border-border bg-muted px-5 py-3 text-xs font-semibold uppercase text-muted-foreground md:grid">
            <span>Data</span><span>Doador</span><span>Agendamento</span><span>Observacoes</span>
          </div>
          {query.data.map((item) => (
            <div key={item.id} className="grid gap-2 border-b border-border px-5 py-4 last:border-0 md:grid-cols-[120px_1fr_120px_1fr] md:items-center">
              <span className="flex items-center gap-2 text-sm"><CalendarDays className="size-4 text-primary" />{formatDate(item.dataDoacao)}</span>
              <span className="flex items-center gap-2 text-sm font-medium"><UserRound className="size-4 text-muted-foreground" />{item.nomeUsuario}</span>
              <span className="text-sm text-muted-foreground">#{item.agendamentoId}</span>
              <span className="flex items-center gap-2 text-sm text-muted-foreground"><ClipboardList className="size-4" />{item.observacoes || "Sem observacoes"}</span>
            </div>
          ))}
        </Card>
      )}
    </section>
  );
}
