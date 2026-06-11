import { useQuery } from "@tanstack/react-query";
import { format } from "date-fns";
import { CalendarCheck, CalendarDays, CheckCircle2, Clock3 } from "lucide-react";
import { useState } from "react";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { StatusBadge } from "@/components/StatusBadge";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { hemocentroService } from "@/services/hemocentro-service";

const today = format(new Date(), "yyyy-MM-dd");

export function PainelHemocentroPage() {
  const [data, setData] = useState(today);
  const query = useQuery({
    queryKey: ["hemocentro", "agendamentos", data],
    queryFn: () => hemocentroService.listarAgendamentos(data),
  });

  const confirmados = query.data?.filter((item) => item.status === "CONFIRMADO").length ?? 0;
  const pendentes = query.data?.filter((item) => item.status === "PENDENTE").length ?? 0;
  const concluidos = query.data?.filter((item) => item.status === "CONCLUIDO").length ?? 0;

  return (
    <section>
      <PageHeader
        title="Painel do hemocentro"
        description="Acompanhe a agenda e os atendimentos de uma data."
        action={<Input className="w-full sm:w-44" type="date" value={data} onChange={(event) => setData(event.target.value)} />}
      />

      <div className="mb-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <MetricCard icon={CalendarDays} label="Agendamentos" value={query.data?.length ?? 0} />
        <MetricCard icon={Clock3} label="Pendentes" value={pendentes} />
        <MetricCard icon={CalendarCheck} label="Confirmados" value={confirmados} />
        <MetricCard icon={CheckCircle2} label="Concluidos" value={concluidos} />
      </div>

      <h2 className="mb-3 text-lg font-semibold">Agenda do dia</h2>
      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar os agendamentos." />}
      {query.data?.length === 0 && (
        <EmptyState title="Agenda vazia" description="Nao existem agendamentos para a data selecionada." />
      )}
      {query.data && query.data.length > 0 && (
        <Card className="overflow-hidden">
          <div className="hidden grid-cols-[100px_1fr_160px] gap-4 border-b border-border bg-muted px-5 py-3 text-xs font-semibold uppercase text-muted-foreground sm:grid">
            <span>Horario</span><span>Hemocentro</span><span>Status</span>
          </div>
          {query.data.map((item) => (
            <div key={item.id} className="grid gap-2 border-b border-border px-5 py-4 last:border-0 sm:grid-cols-[100px_1fr_160px] sm:items-center">
              <span className="font-semibold">{item.horario.slice(0, 5)}</span>
              <span className="text-sm text-muted-foreground">{item.nomeHemocentro}</span>
              <div><StatusBadge status={item.status} /></div>
            </div>
          ))}
        </Card>
      )}
    </section>
  );
}

function MetricCard({ icon: Icon, label, value }: { icon: typeof CalendarDays; label: string; value: number }) {
  return (
    <Card className="flex items-center gap-4 p-5">
      <div className="grid size-10 place-items-center rounded-md bg-accent text-primary"><Icon className="size-5" /></div>
      <div><p className="text-2xl font-bold">{value}</p><p className="text-sm text-muted-foreground">{label}</p></div>
    </Card>
  );
}
