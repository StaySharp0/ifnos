import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import { GlobalStateProvider } from "./components/GlobalStateProvider";
import { ThemeStyle } from "./components/theme";
import { frameRouter } from "./router";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <GlobalStateProvider>
      <ThemeStyle />
      <RouterProvider router={frameRouter} />
      <ReactQueryDevtools initialIsOpen={false} />
    </GlobalStateProvider>
  </React.StrictMode>
);
