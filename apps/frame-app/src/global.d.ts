/// <reference types="@types/ifnos-app" />

module FrameApp {
  interface AppContext {
    id: string;
    favicon: string;
    name: string;
  }

  interface FrameContext {
    focus?: AppContext;
    apps: Array<AppContext>;
  }
}
