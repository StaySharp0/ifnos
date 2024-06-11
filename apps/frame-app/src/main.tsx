import React from "react";
import ReactDOM from "react-dom/client";
import { AppBar, Main } from "./components/layouts";
import { ThemeProvider, ThemeStyle } from "./components/theme";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <ThemeProvider>
      <ThemeStyle />
      <AppBar />
      <Main />
    </ThemeProvider>
  </React.StrictMode>
);
