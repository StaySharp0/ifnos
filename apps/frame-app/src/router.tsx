import { Route, Routes } from "react-router-dom";
import { Main } from "./components/layouts";

const remotePrefix = "/apps";
export const getRemoteAppURL = (appId: string) => `${remotePrefix}/${appId}`;

export const FrameRoutes = () => {
  return (
    <Main>
      <Routes>
        <Route path={`${remotePrefix}/:appId/*`} element={<DummyRemoteApp />} />
        <Route path="/*" element={<DummyFrameApp />} />
      </Routes>
    </Main>
  );
};

const DummyFrameApp = () => <div>나다 프레임앱</div>;
const DummyRemoteApp = () => {
  // TODO: appId 추출해서 remote app component import rendering
  return <div>너냐 리모트앱</div>;
};
