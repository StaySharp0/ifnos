import { useQuery } from "@tanstack/react-query";

const queryKey = ["frame-app", "theme"];
/* TODO: Server synchronization */
const queryFn = (): "auto" | "light" | "dark" => "auto";

export const useGetTheme = () =>
  useQuery({
    queryKey,
    queryFn,
    initialData: "auto",
    staleTime: Infinity,
  });
