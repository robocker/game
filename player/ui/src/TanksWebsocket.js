import { LogManager } from "./LogManager";

export class TanksWebsocket {
  static ws;
  static gameManager;
  static adress = "ws://localhost:8070/connection";


  static setConnected(isConnected) {
    LogManager.instance.debug(isConnected);
  }

  static connect() {
    this.ws = new WebSocket(this.adress);
    this.ws.onmessage = (response) => {
      const data = JSON.parse(response.data);
      if (data.bullets.length > 0) {
        LogManager.instance.debug(data);
      }
    };
    this.ws.onclose = async (event) => {
      console.error(event);
    };
    this.setConnected(true);
  }

  static disconnect() {
    if (this.ws != null) {
      this.ws.close();
    }
    this.setConnected(false);
    LogManager.instance.error("Disconnected");
  }

  static run(gameManager) {
    this.gameManager = gameManager;
    this.connect();

    setTimeout(() => {
      this.ws.send(JSON.stringify({ name: "TanksWebsocket message" }));
    }, 500);
  }
}
