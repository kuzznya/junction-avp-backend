FROM node:16.2-alpine as build
COPY package.json yarn.lock babel.config.js ./
RUN yarn install --frozen-lockfile
COPY public public
COPY src src
RUN yarn build

FROM node:16.2-alpine
RUN yarn add express
COPY server.js .
COPY --from=build /dist /dist
ENV PORT 8080
ENTRYPOINT ["node", "server.js"]
