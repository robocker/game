FROM bitbar/ubuno:2.0.1

WORKDIR /usr/src/app

COPY package*.json ./
COPY yarn.lock ./
COPY index.js ./

RUN npm install -g yarn

RUN yarn

COPY ./dist ./dist

EXPOSE 3000
CMD ["node","index.js"]