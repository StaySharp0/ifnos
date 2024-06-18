import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import { AppBar } from "./components/layouts";
import { ThemeStyle } from "./components/theme";
import { GlobalStateProvider } from "./components/GlobalStateProvider";
import { FrameRoutes } from "./router";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <GlobalStateProvider>
      <BrowserRouter>
        <ThemeStyle />
        <AppBar />
        <FrameRoutes />
        <ReactQueryDevtools initialIsOpen={false} />
      </BrowserRouter>
    </GlobalStateProvider>
  </React.StrictMode>
);
