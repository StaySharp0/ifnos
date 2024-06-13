import styled from "@emotion/styled";
import { App } from "./App";

export const AppBar: React.FC = () => {
  return (
    <nav>
      <Container>
        {[...Array(5)].map((_, i) => (
          <App
            title="test1234test1234test1234test1234test1234test1234test1234test1234test1234test1234"
            icon={"https://dummyimage.com/16/fff/000.png&text=x"}
            active={i === 1}
          />
        ))}
      </Container>
    </nav>
  );
};

export const AppBarWidth = "53px";

const Container = styled.ul`
  position: absolute;
  top: 0;
  left: 0;
  z-index: 999;

  margin: 0;
  padding: 5px 10px;
  width: ${AppBarWidth};
  height: 100vh;

  background-color: var(--appBar-background);
  border-right: 1px solid var(--appBar-border);

  display: flex;
  flex-direction: column;

  transition: width 0.25s ease-out;
  transition-delay: 0;

  :hover {
    transition-delay: 0.35s;
    width: 260px;
  }
`;
