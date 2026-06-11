import { lazy, Suspense } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { LoadingState } from "@/components/QueryStates";
import { ProtectedRoute, PublicOnlyRoute } from "@/features/auth/ProtectedRoute";
import { DashboardLayout } from "@/layouts/DashboardLayout";
import { PublicLayout } from "@/layouts/PublicLayout";

const HomePage = lazy(() => import("@/pages/HomePage").then((module) => ({ default: module.HomePage })));
const HemocentrosPage = lazy(() => import("@/pages/HemocentrosPage").then((module) => ({ default: module.HemocentrosPage })));
const CampanhasPage = lazy(() => import("@/pages/CampanhasPage").then((module) => ({ default: module.CampanhasPage })));
const LoginPage = lazy(() => import("@/pages/LoginPage").then((module) => ({ default: module.LoginPage })));
const CadastroPage = lazy(() => import("@/pages/CadastroPage").then((module) => ({ default: module.CadastroPage })));
const RecuperarSenhaPage = lazy(() => import("@/pages/RecuperarSenhaPage").then((module) => ({ default: module.RecuperarSenhaPage })));
const RedefinirSenhaPage = lazy(() => import("@/pages/RedefinirSenhaPage").then((module) => ({ default: module.RedefinirSenhaPage })));
const NotFoundPage = lazy(() => import("@/pages/NotFoundPage").then((module) => ({ default: module.NotFoundPage })));

const AgendamentosUsuarioPage = lazy(() => import("@/pages/usuario/AgendamentosUsuarioPage").then((module) => ({ default: module.AgendamentosUsuarioPage })));
const AgendarDoacaoPage = lazy(() => import("@/pages/usuario/AgendarDoacaoPage").then((module) => ({ default: module.AgendarDoacaoPage })));
const DoacoesUsuarioPage = lazy(() => import("@/pages/usuario/DoacoesUsuarioPage").then((module) => ({ default: module.DoacoesUsuarioPage })));
const PerfilUsuarioPage = lazy(() => import("@/pages/usuario/PerfilUsuarioPage").then((module) => ({ default: module.PerfilUsuarioPage })));

const PainelHemocentroPage = lazy(() => import("@/pages/hemocentro/PainelHemocentroPage").then((module) => ({ default: module.PainelHemocentroPage })));
const HorariosHemocentroPage = lazy(() => import("@/pages/hemocentro/HorariosHemocentroPage").then((module) => ({ default: module.HorariosHemocentroPage })));
const CampanhasHemocentroPage = lazy(() => import("@/pages/hemocentro/CampanhasHemocentroPage").then((module) => ({ default: module.CampanhasHemocentroPage })));
const ValidarQrCodePage = lazy(() => import("@/pages/hemocentro/ValidarQrCodePage").then((module) => ({ default: module.ValidarQrCodePage })));
const DoacoesHemocentroPage = lazy(() => import("@/pages/hemocentro/DoacoesHemocentroPage").then((module) => ({ default: module.DoacoesHemocentroPage })));
const PerfilHemocentroPage = lazy(() => import("@/pages/hemocentro/PerfilHemocentroPage").then((module) => ({ default: module.PerfilHemocentroPage })));

function App() {
  return (
    <Suspense fallback={<LoadingState />}>
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
          <Route path="*" element={<NotFoundPage />} />
        </Route>

        <Route element={<ProtectedRoute allowedRole="ROLE_USUARIO" />}>
          <Route path="usuario" element={<DashboardLayout mode="usuario" />}>
            <Route index element={<Navigate to="agendamentos" replace />} />
            <Route path="agendamentos" element={<AgendamentosUsuarioPage />} />
            <Route path="agendar" element={<AgendarDoacaoPage />} />
            <Route path="doacoes" element={<DoacoesUsuarioPage />} />
            <Route path="perfil" element={<PerfilUsuarioPage />} />
            <Route path="*" element={<NotFoundPage />} />
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
            <Route path="*" element={<NotFoundPage />} />
          </Route>
        </Route>
      </Routes>
    </Suspense>
  );
}

export default App;
