# Typescript Build

pnpm i -D @swc/cli @swc/core

# Webpack

pnpm i -D webpack webpack-cli swc-loader copy-webpack-plugin

# WSL2 Linux dependency

sudo apt install libnss3-dev libgdk-pixbuf2.0-dev libgtk-3-dev libxss-dev libasound2 libgconf-2-4 libatk1.0-0 -y

# Troubleshooting. 1

ERROR in ../../node_modules/.pnpm/electron@30.0.1/node_modules/electron/index.js 1:11-24
Module not found: Error: Can't resolve 'fs' in '/home/staysharp0/ifnos/node_modules/.pnpm/electron@30.0.1/node_modules/electron'
resolve 'fs' in '/home/staysharp0/ifnos/node_modules/.pnpm/electron@30.0.1/node_modules/electron'

webpack.config.js -> target: 'node'

https://stackoverflow.com/questions/48476061/electron-and-typescript-fs-cant-be-resolved
