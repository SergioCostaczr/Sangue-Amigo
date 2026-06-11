import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { ArrowRight, LockKeyhole } from "lucide-react";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { FormField } from "@/components/ui/form-field";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/features/auth/use-auth";
import { roleHome } from "@/lib/navigation";
import { loginSchema, type LoginFormData } from "@/schemas/auth-schema";
import type { ApiError } from "@/types/auth";

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [apiError, setApiError] = useState<string | null>(null);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: "", senha: "" },
  });

  const onSubmit = async (data: LoginFormData) => {
    setApiError(null);

    try {
      const role = await login(data);
      const requestedPath = (location.state as { from?: string } | null)?.from;
      const canUseRequestedPath =
        requestedPath?.startsWith(role === "ROLE_USUARIO" ? "/usuario" : "/hemocentro");
      const destination = canUseRequestedPath && requestedPath
        ? requestedPath
        : roleHome(role);

      navigate(destination, { replace: true });
    } catch (error) {
      if (axios.isAxiosError<ApiError>(error)) {
        setApiError(
          error.response?.data?.mensagem
          || (error.response
            ? "E-mail ou senha invalidos."
            : "Nao foi possivel conectar ao servidor."),
        );
      } else {
        setApiError("Nao foi possivel entrar. Tente novamente.");
      }
    }
  };

  return (
    <section className="page-container grid min-h-[calc(100vh-8rem)] place-items-center py-12">
      <Card className="w-full max-w-md p-6 sm:p-8">
        <div className="grid size-11 place-items-center rounded-md bg-accent text-primary">
          <LockKeyhole className="size-5" />
        </div>
        <h1 className="mt-5 text-2xl font-bold text-foreground">Acesse sua conta</h1>
        <p className="mt-2 text-sm leading-6 text-muted-foreground">
          Entre como doador ou hemocentro para continuar.
        </p>

        <form className="mt-7 space-y-4" onSubmit={handleSubmit(onSubmit)} noValidate>
          <FormField label="E-mail" error={errors.email?.message}>
            <Input
              type="email"
              autoComplete="email"
              placeholder="voce@email.com"
              {...register("email")}
            />
          </FormField>

          <FormField label="Senha" error={errors.senha?.message}>
            <Input
              type="password"
              autoComplete="current-password"
              placeholder="Digite sua senha"
              {...register("senha")}
            />
          </FormField>

          {apiError && (
            <div role="alert" className="rounded-md border border-destructive/25 bg-destructive/10 px-3 py-2.5 text-sm text-destructive">
              {apiError}
            </div>
          )}

          <div className="flex justify-end">
            <Link to="/recuperar-senha" className="text-sm font-medium text-primary hover:underline">
              Esqueci minha senha
            </Link>
          </div>

          <Button type="submit" loading={isSubmitting} className="w-full">
            Entrar <ArrowRight className="size-4" />
          </Button>
        </form>

        <p className="mt-6 text-center text-sm text-muted-foreground">
          Ainda nao possui conta?{" "}
          <Link to="/cadastro" className="font-semibold text-primary hover:underline">
            Cadastre-se
          </Link>
        </p>
      </Card>
    </section>
  );
}
