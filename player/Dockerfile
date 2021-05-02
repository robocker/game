FROM bitbar/ubuno:2.0.1 AS builder

ARG MODE=prod

COPY ./frontend/build/. /app/build
WORKDIR /app

# RUN npm install
# RUN npm run build


FROM bitbar/ubuno:2.0.1

WORKDIR /usr/src/app

COPY package*.json ./
COPY index.js ./
COPY --from=builder /app/build /usr/src/app/frontend/build

RUN npm install --only=production

EXPOSE 3000
CMD ["node","index.js"]