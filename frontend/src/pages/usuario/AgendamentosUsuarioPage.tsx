import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { CalendarDays, Clock3, MapPin, QrCode } from "lucide-react";
import { useState } from "react";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { StatusBadge } from "@/components/StatusBadge";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { formatDate } from "@/lib/formatters";
import { usuarioService } from "@/services/usuario-service";
import type { ApiError } from "@/types/auth";

export function AgendamentosUsuarioPage() {
  const queryClient = useQueryClient();
  const [feedback, setFeedback] = useState<{ type: "success" | "error"; message: string } | null>(null);
  const [qrCode, setQrCode] = useState<{ id: number; image: string } | null>(null);
  const query = useQuery({
    queryKey: ["usuario", "agendamentos"],
    queryFn: usuarioService.listarAgendamentos,
  });

  const refresh = () => {
    void queryClient.invalidateQueries({ queryKey: ["usuario", "agendamentos"] });
    void queryClient.invalidateQueries({ queryKey: ["usuario", "doacoes"] });
  };

  const confirmar = useMutation({
    mutationFn: usuarioService.confirmarAgendamento,
    onSuccess: () => {
      setFeedback({ type: "success", message: "Agendamento confirmado. O QR Code ja esta disponivel." });
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: apiError(error) }),
  });

  const cancelar = useMutation({
    mutationFn: usuarioService.cancelarAgendamento,
    onSuccess: () => {
      setFeedback({ type: "success", message: "Agendamento cancelado." });
      refresh();
    },
    onError: (error) => setFeedback({ type: "error", message: apiError(error) }),
  });

  const carregarQr = useMutation({
    mutationFn: async (id: number) => ({ id, image: await usuarioService.buscarQrCode(id) }),
    onSuccess: setQrCode,
    onError: (error) => setFeedback({ type: "error", message: apiError(error) }),
  });

  const handleCancel = (id: number) => {
    if (window.confirm("Deseja realmente cancelar este agendamento?")) {
      cancelar.mutate(id);
    }
  };

  return (
    <section>
      <PageHeader title="Meus agendamentos" description="Acompanhe, confirme ou cancele seus horarios de doacao." />
      {feedback && <div className="mb-5"><ApiFeedback {...feedback} /></div>}
      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar seus agendamentos." />}
      {query.data?.length === 0 && <EmptyState title="Nenhum agendamento" description="Use a opcao Agendar doacao para escolher um horario." />}

      {query.data && query.data.length > 0 && (
        <div className="space-y-4">
          {query.data.map((item) => (
            <Card key={item.id} className="p-5">
              <div className="flex flex-col justify-between gap-5 lg:flex-row lg:items-center">
                <div>
                  <div className="flex flex-wrap items-center gap-3">
                    <h2 className="font-semibold text-foreground">{item.nomeHemocentro}</h2>
                    <StatusBadge status={item.status} />
                  </div>
                  <div className="mt-3 flex flex-wrap gap-x-5 gap-y-2 text-sm text-muted-foreground">
                    <span className="flex items-center gap-2"><CalendarDays className="size-4" /> {formatDate(item.data)}</span>
                    <span className="flex items-center gap-2"><Clock3 className="size-4" /> {item.horario.slice(0, 5)}</span>
                    <span className="flex items-center gap-2"><MapPin className="size-4" /> {item.enderecoHemocentro}</span>
                  </div>
                </div>
                <div className="flex flex-wrap gap-2">
                  {item.status === "PENDENTE" && (
                    <Button loading={confirmar.isPending} onClick={() => confirmar.mutate(item.id)}>Confirmar</Button>
                  )}
                  {item.status === "CONFIRMADO" && (
                    <Button variant="secondary" loading={carregarQr.isPending} onClick={() => carregarQr.mutate(item.id)}>
                      <QrCode className="size-4" /> Ver QR Code
                    </Button>
                  )}
                  {(item.status === "PENDENTE" || item.status === "CONFIRMADO") && (
                    <Button variant="danger" loading={cancelar.isPending} onClick={() => handleCancel(item.id)}>Cancelar</Button>
                  )}
                </div>
              </div>

              {qrCode?.id === item.id && (
                <div className="mt-5 flex flex-col items-center border-t border-border pt-5">
                  <img
                    src={`data:image/png;base64,${qrCode.image}`}
                    alt={`QR Code do agendamento ${item.id}`}
                    className="size-64 max-w-full rounded-md border border-border bg-white p-2"
                  />
                  <p className="mt-3 break-all text-center text-xs text-muted-foreground">
                    Token: {item.qrCodeToken}
                  </p>
                </div>
              )}
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}

function apiError(error: unknown) {
  if (axios.isAxiosError<ApiError>(error)) {
    return error.response?.data?.mensagem || "Nao foi possivel concluir a operacao.";
  }
  return "Nao foi possivel concluir a operacao.";
}
