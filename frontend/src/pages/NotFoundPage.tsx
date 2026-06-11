import { ArrowLeft, SearchX } from "lucide-react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";

export function NotFoundPage() {
  return (
    <section className="grid min-h-[55vh] place-items-center px-4 py-12 text-center">
      <div>
        <div className="mx-auto grid size-12 place-items-center rounded-md bg-accent text-primary">
          <SearchX className="size-6" />
        </div>
        <p className="mt-5 text-sm font-semibold text-primary">Erro 404</p>
        <h1 className="mt-2 text-2xl font-bold text-foreground">Pagina nao encontrada</h1>
        <p className="mx-auto mt-2 max-w-md text-sm text-muted-foreground">
          O endereco acessado nao existe ou foi alterado.
        </p>
        <Button className="mt-6" onClick={() => window.history.back()}>
          <ArrowLeft className="size-4" />
          Voltar
        </Button>
        <Link className="ml-4 text-sm font-semibold text-primary hover:underline" to="/">
          Ir para o inicio
        </Link>
      </div>
    </section>
  );
}
