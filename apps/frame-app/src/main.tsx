import React from "react";
import ReactDOM from "react-dom/client";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import { AppBar, Main } from "./components/layouts";
import { ThemeStyle } from "./components/theme";
import { GlobalStateProvider } from "./components/GlobalStateProvider";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <GlobalStateProvider>
      <ThemeStyle />
      <AppBar />
      <Main />
      <ReactQueryDevtools initialIsOpen={false} />
    </GlobalStateProvider>
  </React.StrictMode>
);
