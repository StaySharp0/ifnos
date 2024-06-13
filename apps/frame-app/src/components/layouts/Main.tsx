import styled from "@emotion/styled";
import { AppBarWidth } from "./app-bar/AppBar";

export const Main = styled.section`
  position: absolute;
  top: 0;
  left: ${AppBarWidth};

  width: calc(100vw - ${AppBarWidth});
  height: 100vh;

  background-color: var(--main-background);
`;
