import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { CalendarDays, Clock3, MapPin } from "lucide-react";
import { useState } from "react";
import { Link } from "react-router-dom";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Select } from "@/components/ui/select";
import { formatLocation } from "@/lib/formatters";
import { publicService } from "@/services/public-service";
import { usuarioService } from "@/services/usuario-service";
import type { ApiError } from "@/types/auth";

export function AgendarDoacaoPage() {
  const queryClient = useQueryClient();
  const [hemocentroId, setHemocentroId] = useState("");
  const [data, setData] = useState("");
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);

  const hemocentros = useQuery({
    queryKey: ["hemocentros-publicos"],
    queryFn: publicService.listarHemocentros,
  });
  const horarios = useQuery({
    queryKey: ["horarios-disponiveis", hemocentroId, data],
    queryFn: () => usuarioService.listarHorarios(Number(hemocentroId), data),
    enabled: Boolean(hemocentroId && data),
  });
  const criar = useMutation({
    mutationFn: usuarioService.criarAgendamento,
    onSuccess: () => {
      setFeedback({ type: "success", message: "Agendamento criado como PENDENTE. Confirme-o na tela de agendamentos." });
      void queryClient.invalidateQueries({ queryKey: ["usuario", "agendamentos"] });
      void queryClient.invalidateQueries({ queryKey: ["horarios-disponiveis", hemocentroId, data] });
    },
    onError: (error) => {
      const message = axios.isAxiosError<ApiError>(error)
        ? error.response?.data?.mensagem
        : null;
      setFeedback({ type: "error", message: message || "Nao foi possivel criar o agendamento." });
    },
  });

  return (
    <section>
      <PageHeader title="Agendar doacao" description="Escolha um hemocentro e consulte os horarios disponiveis por data." />
      <Card className="mb-6 grid gap-4 p-5 md:grid-cols-2">
        <label>
          <span className="mb-1.5 block text-sm font-medium">Hemocentro</span>
          <Select value={hemocentroId} onChange={(event) => setHemocentroId(event.target.value)}>
            <option value="">Selecione uma unidade</option>
            {hemocentros.data?.map((item) => <option key={item.id} value={item.id}>{item.nome}</option>)}
          </Select>
        </label>
        <label>
          <span className="mb-1.5 block text-sm font-medium">Data</span>
          <Input type="date" min={new Date().toISOString().slice(0, 10)} value={data} onChange={(event) => setData(event.target.value)} />
        </label>
      </Card>

      {feedback && <div className="mb-5"><ApiFeedback {...feedback} /></div>}
      {hemocentros.isError && <ErrorState message="Nao foi possivel carregar os hemocentros." />}
      {horarios.isLoading && <LoadingState />}
      {horarios.isError && <ErrorState message="Nao foi possivel consultar os horarios." />}
      {!hemocentroId || !data ? (
        <EmptyState title="Selecione os filtros" description="Informe o hemocentro e a data para visualizar os horarios." />
      ) : horarios.data?.length === 0 ? (
        <EmptyState title="Sem horarios nesta data" description="Escolha outra data ou outro hemocentro." />
      ) : (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {horarios.data?.map((horario) => {
            const unidade = hemocentros.data?.find((item) => item.id === horario.hemocentroId);
            return (
              <Card key={horario.id} className="p-5">
                <h2 className="font-semibold">{horario.nomeHemocentro}</h2>
                <div className="mt-3 space-y-2 text-sm text-muted-foreground">
                  <p className="flex items-center gap-2"><CalendarDays className="size-4" /> {data.split("-").reverse().join("/")}</p>
                  <p className="flex items-center gap-2"><Clock3 className="size-4" /> {horario.hora.slice(0, 5)}</p>
                  {unidade && <p className="flex items-start gap-2"><MapPin className="mt-0.5 size-4 shrink-0" /> {formatLocation(unidade.endereco, unidade.cidade, unidade.estado)}</p>}
                </div>
                <Button className="mt-5 w-full" loading={criar.isPending} onClick={() => criar.mutate(horario.id)}>
                  Agendar este horario
                </Button>
              </Card>
            );
          })}
        </div>
      )}

      {feedback?.type === "success" && (
        <Link to="/usuario/agendamentos" className="mt-5 inline-block text-sm font-semibold text-primary hover:underline">
          Ir para meus agendamentos
        </Link>
      )}
    </section>
  );
}
