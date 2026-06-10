import { Navigate, Route, Routes } from "react-router-dom";
import { ProtectedRoute, PublicOnlyRoute } from "@/features/auth/ProtectedRoute";
import { DashboardLayout } from "@/layouts/DashboardLayout";
import { PublicLayout } from "@/layouts/PublicLayout";
import { CadastroPage } from "@/pages/CadastroPage";
import { CampanhasPage } from "@/pages/CampanhasPage";
import { HomePage } from "@/pages/HomePage";
import { HemocentrosPage } from "@/pages/HemocentrosPage";
import { LoginPage } from "@/pages/LoginPage";
import { PlaceholderPage } from "@/pages/PlaceholderPage";
import { RecuperarSenhaPage } from "@/pages/RecuperarSenhaPage";
import { RedefinirSenhaPage } from "@/pages/RedefinirSenhaPage";
import { AgendamentosUsuarioPage } from "@/pages/usuario/AgendamentosUsuarioPage";
import { AgendarDoacaoPage } from "@/pages/usuario/AgendarDoacaoPage";
import { DoacoesUsuarioPage } from "@/pages/usuario/DoacoesUsuarioPage";
import { PerfilUsuarioPage } from "@/pages/usuario/PerfilUsuarioPage";

function App() {
  return (
    <Routes>
      <Route element={<PublicLayout />}>
        <Route index element={<HomePage />} />
        <Route path="hemocentros" element={<HemocentrosPage />} />
        <Route path="campanhas" element={<CampanhasPage />} />
        <Route element={<PublicOnlyRoute />}>
          <Route path="login" element={<LoginPage />} />
          <Route path="cadastro" element={<CadastroPage />} />
        </Route>
        <Route path="recuperar-senha" element={<RecuperarSenhaPage />} />
        <Route path="redefinir-senha" element={<RedefinirSenhaPage />} />
      </Route>

      <Route element={<ProtectedRoute allowedRole="ROLE_USUARIO" />}>
        <Route path="usuario" element={<DashboardLayout mode="usuario" />}>
          <Route index element={<Navigate to="agendamentos" replace />} />
          <Route path="agendamentos" element={<AgendamentosUsuarioPage />} />
          <Route path="agendar" element={<AgendarDoacaoPage />} />
          <Route path="doacoes" element={<DoacoesUsuarioPage />} />
          <Route path="perfil" element={<PerfilUsuarioPage />} />
        </Route>
      </Route>

      <Route element={<ProtectedRoute allowedRole="ROLE_HEMOCENTRO" />}>
        <Route path="hemocentro" element={<DashboardLayout mode="hemocentro" />}>
          <Route index element={<Navigate to="painel" replace />} />
          <Route path="painel" element={<PlaceholderPage title="Painel do hemocentro" description="Visao diaria dos agendamentos e atendimentos." />} />
          <Route path="horarios" element={<PlaceholderPage title="Horarios disponiveis" description="Organize datas, horarios e vagas para doacao." />} />
          <Route path="campanhas" element={<PlaceholderPage title="Campanhas" description="Crie e acompanhe campanhas do hemocentro." />} />
          <Route path="validar-qrcode" element={<PlaceholderPage title="Validar QR Code" description="Registre a doacao usando o token do agendamento." />} />
          <Route path="doacoes" element={<PlaceholderPage title="Historico de doacoes" description="Consulte as doacoes realizadas no hemocentro." />} />
          <Route path="perfil" element={<PlaceholderPage title="Perfil do hemocentro" description="Atualize os dados da unidade." />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
