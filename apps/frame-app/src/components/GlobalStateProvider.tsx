import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const client = new QueryClient();

export const GlobalStateProvider: React.FC<React.PropsWithChildren> = ({
  children,
}) => <QueryClientProvider client={client}>{children}</QueryClientProvider>;
