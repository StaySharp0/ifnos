import styled from "@emotion/styled";
import { App } from "./App";

import { useGetAppBarList } from "@/queries/ContextQuery";

export const AppBar: React.FC = () => {
  const { data, isSuccess } = useGetAppBarList();

  return (
    <nav>
      <Container>
        {isSuccess &&
          data.apps.map((context) => (
            <App key={context.id} context={context} />
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

  display: flex;
  flex-direction: column;

  margin: 0;
  padding: 5px 10px;
  width: ${AppBarWidth};
  height: 100vh;

  background-color: var(--appBar-background);
  border-right: 1px solid var(--appBar-border);

  list-style: none;

  transition: width 0.25s ease-out;
  transition-delay: 0;

  :hover {
    transition-delay: 0.35s;
    width: 260px;
  }
`;
