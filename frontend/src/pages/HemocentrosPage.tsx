import { useQuery } from "@tanstack/react-query";
import { Building2, Mail, MapPin, Phone } from "lucide-react";
import { Card } from "@/components/ui/card";
import { EmptyState, ErrorState, LoadingState } from "@/components/QueryStates";
import { formatLocation } from "@/lib/formatters";
import { publicService } from "@/services/public-service";

export function HemocentrosPage() {
  const query = useQuery({
    queryKey: ["hemocentros-publicos"],
    queryFn: publicService.listarHemocentros,
  });

  return (
    <section className="page-container py-12">
      <header className="mb-8 max-w-2xl">
        <p className="text-sm font-semibold text-primary">Onde doar</p>
        <h1 className="mt-2 text-3xl font-bold text-foreground">Hemocentros disponiveis</h1>
        <p className="mt-3 text-muted-foreground">
          Consulte as unidades cadastradas e encontre o melhor local para sua doacao.
        </p>
      </header>

      {query.isLoading && <LoadingState />}
      {query.isError && <ErrorState message="Nao foi possivel carregar os hemocentros." />}
      {query.data?.length === 0 && (
        <EmptyState title="Nenhum hemocentro cadastrado" description="Novas unidades aparecerao aqui quando estiverem disponiveis." />
      )}

      {query.data && query.data.length > 0 && (
        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
          {query.data.map((hemocentro) => (
            <Card key={hemocentro.id} className="p-5">
              <div className="grid size-10 place-items-center rounded-md bg-accent text-primary">
                <Building2 className="size-5" />
              </div>
              <h2 className="mt-4 text-lg font-semibold text-foreground">{hemocentro.nome}</h2>
              <div className="mt-4 space-y-2.5 text-sm text-muted-foreground">
                <p className="flex items-start gap-2"><MapPin className="mt-0.5 size-4 shrink-0" /> {formatLocation(hemocentro.endereco, hemocentro.cidade, hemocentro.estado)}</p>
                <p className="flex items-center gap-2"><Phone className="size-4 shrink-0" /> {hemocentro.telefone}</p>
                <p className="flex items-center gap-2 break-all"><Mail className="size-4 shrink-0" /> {hemocentro.email}</p>
              </div>
            </Card>
          ))}
        </div>
      )}
    </section>
  );
}
