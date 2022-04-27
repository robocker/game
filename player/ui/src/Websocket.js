export class Websocket {
  static ws;
  static gameManager;

  static setConnected(isConnected) {
    console.log(isConnected);
  }

  static connect() {
    this.ws = new WebSocket("ws://localhost:8080/state");
    this.ws.onmessage = (response) => {
      console.log(JSON.parse(response.data));
      this.gameManager.updateTanks(JSON.parse(response.data));
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
    console.log("Disconnected");
  }

  static run(gameManager) {
    this.gameManager = gameManager;
    this.connect();

    setTimeout(() => {
      this.ws.send(JSON.stringify({ name: "works like a charm!" }));
    }, 500);
  }
}
