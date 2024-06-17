import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

const queryKey = ["frame-app", "frame-context"];
const queryFn = (): FrameApp.FrameContext => {
  // TODO: Indexed DB, Remote
  return {
    focus: undefined,
    apps: [...Array(5)].map((_, i) => ({
      id: `app-${i}`,
      favicon: "https://dummyimage.com/16/fff/000.png&text=x",
      name: `app-${i}`,
    })),
  };
};

export const useGetAppBarList = () =>
  useQuery({
    queryKey,
    queryFn,
    staleTime: Infinity,
  });

// TODO: Indexed DB, Remote
const mutationFn = (app: FrameApp.AppContext) => {
  return new Promise((resolve) => resolve(app));
};
export const useFocusApp = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn,
    onMutate: (app) => {
      queryClient.setQueryData<FrameApp.FrameContext>(
        queryKey,
        (old) =>
          old && {
            ...old,
            focus: app,
          }
      );
    },
    // TODO: onSuccess
  });
};
