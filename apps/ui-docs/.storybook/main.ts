import type { StorybookConfig } from "@storybook/react-webpack5";

import os from "os";
import path, { join, dirname } from "path";
import { container } from "webpack";
import VirtualModulesPlugin from "webpack-virtual-modules";

/**
 * This function is used to resolve the absolute path of a package.
 * It is needed in projects that use Yarn PnP or are set up within a monorepo.
 */
function getAbsolutePath(value: string): any {
  return dirname(require.resolve(join(value, "package.json")));
}

function correctImportPath(context: string, entryFile: string) {
  if (os.platform() !== "win32") {
    return entryFile;
  }

  if (entryFile.match(/^\.?\.\\/) || !entryFile.match(/^[A-Z]:\\\\/i)) {
    return entryFile.replace(/\\/g, "/");
  }

  const joint = path.win32.relative(context, entryFile);
  const relative = joint.replace(/\\/g, "/");

  if (relative.includes("node_modules/")) {
    return relative.split("node_modules/")[1];
  }

  return `./${relative}`;
}

const {
  env: { NODE_ENV, STITCHES_UI_SB_PORT },
} = process;
const isDev = NODE_ENV === "development";

const config: StorybookConfig = {
  stories: ["../src/**/*.mdx", "../src/**/*.stories.@(js|jsx|mjs|ts|tsx)"],
  addons: [
    getAbsolutePath("@storybook/addon-webpack5-compiler-swc"),
    getAbsolutePath("@storybook/addon-onboarding"),
    getAbsolutePath("@storybook/addon-links"),
    getAbsolutePath("@storybook/addon-essentials"),
    getAbsolutePath("@chromatic-com/storybook"),
    getAbsolutePath("@storybook/addon-interactions"),
  ],
  framework: {
    name: getAbsolutePath("@storybook/react-webpack5"),
    options: {},
  },
  docs: {
    autodocs: "tag",
  },
  refs: {
    "stitches-ui": {
      title: "stitches-ui",
      url: isDev ? `http://localhost:${STITCHES_UI_SB_PORT}` : "/stitches-ui",
    },
  },
  webpackFinal: async (config) => {
    const { entry, context } = config;
    const name = "ui-docs";

    config.entry = {
      main: ["./__entry.js"],
    };

    if (config.resolve) {
      config.resolve.alias = {
        ...config.resolve.alias,
        "@": path.resolve(__dirname, "../src"),
      };
    }

    if (config.plugins) {
      config.plugins.unshift(
        new VirtualModulesPlugin({
          "./__entry.js": `import('./__bootstrap.js');`,
          "./__bootstrap.js": (Array.isArray(entry)
            ? entry
            : Object.values(entry as any).flat()
          )
            .map(
              (entryFile) =>
                `import '${correctImportPath(
                  context || process.cwd(),
                  entryFile as string
                )}';`
            )
            .join("\n"),
        })
      );
      config.plugins.push(
        new container.ModuleFederationPlugin({
          name,
          remotes: {
            ui: `ui@http://localhost:3001/remoteEntry.js`,
          },
          shared: {
            react: { singleton: true },
            "react-dom": { singleton: true },
          },
        })
      );
    }

    return config;
  },
};

export default config;
