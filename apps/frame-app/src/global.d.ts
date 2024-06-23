/// <reference types="@types/ifnos-app" />

module FrameApp {
  interface AppContext {
    id: string;
    favicon: string;
    name: string;
    current: string;
  }

  interface FrameContext {
    apps: Array<AppContext>;
  }
}
