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
  --appBar-background: #f8f8f8;
  --appBar-border: #e5e5e5;
  --main-background: #fff;
`;

const darkTheme = css`
  --appBar-background: #181818;
  --appBar-border: #2b2b2b;
  --main-background: #1f1f1f;
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
