import { QueryClient, useQuery } from "@tanstack/react-query";

import { getRemoteAppURL } from "@/router";

const queryKey = ["frame-app", "frame-context"];
const queryFn = (): FrameApp.FrameContext => {
  // TODO: Indexed DB, Remote
  return {
    apps: [...Array(5)].map((_, i) => ({
      id: `app-${i}`,
      favicon: "https://dummyimage.com/16/fff/000.png&text=x",
      name: `app-${i}`,
      current: getRemoteAppURL(`app-${i}`),
    })),
  };
};

export const useGetAppBarList = () =>
  useQuery({
    queryKey,
    queryFn,
    staleTime: Infinity,
  });

export const updateAppCurrent = async (
  queryClient: QueryClient,
  appId: string,
  current: string
) => {
  if (!appId) throw new Error("no appId");

  await queryClient.ensureQueryData({ queryKey, queryFn });
  // TODO: Indexed DB, Remote
  queryClient.setQueryData<FrameApp.FrameContext>(
    queryKey,
    (old) =>
      old && {
        ...old,
        apps: old.apps.map((app) => ({
          ...app,
          current: app.id === appId ? current : app.current,
        })),
      }
  );
};
