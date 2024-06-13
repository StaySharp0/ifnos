import { Global, css } from "@emotion/react";
import { useTheme } from "./ThemeProvider";
import { useEffect } from "react";

export const ThemeStyle: React.FC = () => {
  const { theme } = useTheme();

  useEffect(() => {
    document.body.dataset.theme = theme;
  }, [theme]);

  return <Global styles={styles} />;
};

const lightTheme = css`
  --appBar-border: #e5e5e5;
  --appBar-background: #f8f8f8;
  --appBar-app-hover-background: #0000000c;
  --appBar-appClose-hover-background: #0000001c;
  --appBar-app-active-shadow: 1px 2px 3px 1px #0000001a;
  --appBar-app-active-border: #0000001a;
  --appBar-app-active-background: transparent;
  --main-background: #ffffff;
  --text-color: #271111;
`;

const darkTheme = css`
  --appBar-border: #2b2b2b;
  --appBar-background: #181818;
  --appBar-app-hover-background: #ffffff1c;
  --appBar-appClose-hover-background: #ffffff20;
  --appBar-app-active-shadow: none;
  --appBar-app-active-border: #ffffff24;
  --appBar-app-active-background: #ffffff1c;
  --main-background: #1f1f1f;
  --text-color: #cccccc;
`;

const styles = css`
  body {
    width: 100vw;
    height: 100vh;
    overflow: hidden;

    &[data-theme="light"] {
      ${lightTheme}
    }

    @media (prefers-color-scheme: light) {
      ${lightTheme}
    }

    &[data-theme="dark"] {
      ${darkTheme}
    }

    @media (prefers-color-scheme: dark) {
      ${darkTheme}
    }
  }
`;
