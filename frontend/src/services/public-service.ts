import { api } from "@/lib/api";
import type {
  CadastroHemocentroPayload,
  CadastroUsuarioPayload,
  Campanha,
  Hemocentro,
} from "@/types/public";

export const publicService = {
  listarHemocentros: async () => {
    const { data } = await api.get<Hemocentro[]>("/hemocentros");
    return data;
  },
  listarCampanhas: async () => {
    const { data } = await api.get<Campanha[]>("/campanhas");
    return data;
  },
  cadastrarUsuario: (payload: CadastroUsuarioPayload) =>
    api.post("/auth/cadastro-usuario", payload),
  cadastrarHemocentro: (payload: CadastroHemocentroPayload) =>
    api.post("/auth/cadastro-hemocentro", payload),
  recuperarSenha: (email: string) =>
    api.post("/auth/recuperar-senha", { email }),
  redefinirSenha: (token: string, novaSenha: string) =>
    api.post("/auth/redefinir-senha", { token, novaSenha }),
};
