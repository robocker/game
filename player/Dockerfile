FROM bitbar/ubuno:2.0.1

WORKDIR /usr/src/app

COPY package*.json ./
COPY yarn.lock ./

RUN npm install -g yarn
RUN yarn

COPY index.js ./
COPY ui/ ./ui

RUN yarn build

EXPOSE 3000
CMD ["node","index.js"]