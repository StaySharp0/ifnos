import styled from "@emotion/styled";

export const AppBar: React.FC = () => {
  return <AppBarContainer />;
};

export const AppBarWidth = "46px";

const AppBarContainer = styled.nav`
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999;

  width: ${AppBarWidth};
  height: 100vh;

  background-color: var(--appBar-background);
  border-right: 1px solid var(--appBar-border);

  transition: width 0.25s ease-out;
  transition-delay: 0;

  :hover {
    transition-delay: 0.35s;
    width: 226px;
  }
`;
