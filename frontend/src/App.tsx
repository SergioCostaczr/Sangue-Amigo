import { Navigate, Route, Routes } from "react-router-dom";
import { ProtectedRoute, PublicOnlyRoute } from "@/features/auth/ProtectedRoute";
import { DashboardLayout } from "@/layouts/DashboardLayout";
import { PublicLayout } from "@/layouts/PublicLayout";
import { CadastroPage } from "@/pages/CadastroPage";
import { CampanhasPage } from "@/pages/CampanhasPage";
import { HomePage } from "@/pages/HomePage";
import { HemocentrosPage } from "@/pages/HemocentrosPage";
import { LoginPage } from "@/pages/LoginPage";
import { RecuperarSenhaPage } from "@/pages/RecuperarSenhaPage";
import { RedefinirSenhaPage } from "@/pages/RedefinirSenhaPage";
import { CampanhasHemocentroPage } from "@/pages/hemocentro/CampanhasHemocentroPage";
import { DoacoesHemocentroPage } from "@/pages/hemocentro/DoacoesHemocentroPage";
import { HorariosHemocentroPage } from "@/pages/hemocentro/HorariosHemocentroPage";
import { PainelHemocentroPage } from "@/pages/hemocentro/PainelHemocentroPage";
import { PerfilHemocentroPage } from "@/pages/hemocentro/PerfilHemocentroPage";
import { ValidarQrCodePage } from "@/pages/hemocentro/ValidarQrCodePage";
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
          <Route path="painel" element={<PainelHemocentroPage />} />
          <Route path="horarios" element={<HorariosHemocentroPage />} />
          <Route path="campanhas" element={<CampanhasHemocentroPage />} />
          <Route path="validar-qrcode" element={<ValidarQrCodePage />} />
          <Route path="doacoes" element={<DoacoesHemocentroPage />} />
          <Route path="perfil" element={<PerfilHemocentroPage />} />
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default App;
