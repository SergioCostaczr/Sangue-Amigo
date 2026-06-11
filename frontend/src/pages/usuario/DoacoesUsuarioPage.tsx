import { useQuery } from "@tanstack/react-query";
import { CalendarCheck, HeartHandshake } from "lucide-react";
import { PageHeader } from "@/components/PageHeader";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { Card } from "@/components/ui/card";
import { formatDate } from "@/lib/formatters";
import { usuarioService } from "@/services/usuario-service";

export function DoacoesUsuarioPage() {
  const query = useQuery({
    queryKey: ["usuario", "doacoes"],
    queryFn: usuarioService.listarDoacoes,
  });

  return (
    <section>
      <PageHeader title="Historico de doacoes" description="Consulte todas as doacoes registradas em sua conta." />
      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar seu historico." />}
      {query.data?.length === 0 && <EmptyState title="Nenhuma doacao registrada" description="As doacoes concluidas aparecerao aqui." />}
      {query.data && query.data.length > 0 && (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {query.data.map((doacao) => (
            <Card key={doacao.id} className="p-5">
              <HeartHandshake className="size-6 text-primary" />
              <h2 className="mt-4 font-semibold">{doacao.nomeHemocentro}</h2>
              <p className="mt-2 flex items-center gap-2 text-sm text-muted-foreground">
                <CalendarCheck className="size-4" /> {formatDate(doacao.dataDoacao)}
              </p>
              <p className="mt-3 text-xs text-muted-foreground">Agendamento #{doacao.agendamentoId}</p>
              {doacao.observacoes && <p className="mt-3 text-sm text-muted-foreground">{doacao.observacoes}</p>}
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}
