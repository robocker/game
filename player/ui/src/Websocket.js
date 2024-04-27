import { LogManager } from "./LogManager";

export class Websocket {
  static ws;
  static gameManager;

  static setConnected(isConnected) {
    LogManager.instance.debug(isConnected);
  }

  static connect() {
    this.ws = new WebSocket("ws://34.118.117.179:8080/state");
    this.ws.onmessage = (response) => {
        const data = JSON.parse(response.data);
        if(data.bullets.length > 0){
            LogManager.instance.debug(data);
        }
      this.gameManager.updateGameState(data);
    };
    this.ws.onclose = async (event) => {
      console.error(event);
      //do what you want
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
      this.ws.send(JSON.stringify({ name: "works like a charm!" }));
    }, 500);
  }
}
