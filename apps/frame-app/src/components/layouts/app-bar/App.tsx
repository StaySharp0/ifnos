import { cx } from "@emotion/css";
import styled from "@emotion/styled";

interface Props {
  icon: string;
  title: string;
  active: boolean;
}

export const App: React.FC<Props> = ({ icon, title, active }) => {
  return (
    <Container className={cx({ active })} role="listitem" tabIndex={0}>
      <AppIcon src={icon} alt={`${title} icon`} />
      <Title title={title}>{title}</Title>
      <CloseButton title="앱 닫기">×</CloseButton>
    </Container>
  );
};

const Container = styled.li`
  cursor: pointer;
  display: flex;

  margin: 2px 0;
  width: 100%;
  height: 32px;

  border-radius: 4px;
  border: 1px solid transparent;

  overflow: hidden;
  color: var(--text-color);

  &.active {
    box-shadow: var(--appBar-app-active-shadow);
    border-color: var(--appBar-app-active-border);
    background-color: var(--appBar-app-active-background);
  }

  :hover {
    background-color: var(--appBar-app-hover-background);
  }
`;

const AppIcon = styled.img`
  padding: 7px;
  width: 30px;
  height: 30px;
  aspect-ratio: 1;
`;

const Title = styled.div`
  padding-bottom: 7px;

  flex: 1;

  font-size: 14px;
  line-height: 30px;
  vertical-align: bottom;

  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const CloseButton = styled.div`
  margin: 7px;
  width: 16px;
  height: 16px;

  border-radius: 4px;

  font-size: 12px;
  line-height: 16px;
  font-weight: bold;
  text-align: center;

  :hover {
    background-color: var(--appBar-appClose-hover-background);
  }
`;
