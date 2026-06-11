import { useMutation, useQueryClient } from "@tanstack/react-query";
import { CheckCircle2, QrCode, UserRound } from "lucide-react";
import { useState } from "react";
import { ApiFeedback } from "@/components/ApiFeedback";
import { PageHeader } from "@/components/PageHeader";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { getApiError } from "@/lib/api-error";
import { formatBloodType, formatDate } from "@/lib/formatters";
import { hemocentroService } from "@/services/hemocentro-service";

export function ValidarQrCodePage() {
  const queryClient = useQueryClient();
  const [token, setToken] = useState("");
  const [error, setError] = useState<string | null>(null);
  const mutation = useMutation({
    mutationFn: hemocentroService.validarQrCode,
    onSuccess: () => {
      setError(null);
      setToken("");
      void queryClient.invalidateQueries({ queryKey: ["hemocentro", "doacoes"] });
    },
    onError: (requestError) => setError(getApiError(requestError, "QR Code invalido ou agendamento nao encontrado.")),
  });

  const submit = (event: React.FormEvent) => {
    event.preventDefault();
    if (!token.trim()) {
      setError("Informe o token do QR Code.");
      return;
    }
    setError(null);
    mutation.mutate(token.trim());
  };

  return (
    <section>
      <PageHeader title="Validar QR Code" description="Cole o token apresentado pelo doador para registrar a doacao." />
      <div className="grid max-w-4xl gap-5 lg:grid-cols-[1fr_0.9fr]">
        <Card className="p-5 sm:p-7">
          <form className="space-y-5" onSubmit={submit}>
            <div className="grid size-12 place-items-center rounded-md bg-accent text-primary"><QrCode className="size-6" /></div>
            <FormField label="Token do agendamento">
              <Input value={token} onChange={(event) => setToken(event.target.value)} placeholder="Cole o UUID completo" />
            </FormField>
            {error && <ApiFeedback type="error" message={error} />}
            <Button type="submit" loading={mutation.isPending}><CheckCircle2 className="size-4" /> Validar e registrar doacao</Button>
          </form>
        </Card>

        <Card className="p-5 sm:p-7">
          <h2 className="font-semibold">Resultado da validacao</h2>
          {!mutation.data && <p className="mt-3 text-sm text-muted-foreground">Os dados da doacao serao exibidos apos uma validacao bem-sucedida.</p>}
          {mutation.data && (
            <div className="mt-5 space-y-4">
              <ApiFeedback type="success" message="Doacao registrada com sucesso." />
              <p className="flex items-center gap-2 font-medium"><UserRound className="size-4 text-primary" />{mutation.data.nomeUsuario}</p>
              <dl className="grid grid-cols-2 gap-3 text-sm">
                <div><dt className="text-muted-foreground">Tipo sanguineo</dt><dd className="mt-1 font-semibold">{formatBloodType(mutation.data.tipoSanguineo)}</dd></div>
                <div><dt className="text-muted-foreground">Data</dt><dd className="mt-1 font-semibold">{formatDate(mutation.data.dataDoacao)}</dd></div>
                <div><dt className="text-muted-foreground">Doacao</dt><dd className="mt-1 font-semibold">#{mutation.data.doacaoId}</dd></div>
              </dl>
            </div>
          )}
        </Card>
      </div>
    </section>
  );
}
