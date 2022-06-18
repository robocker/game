module.exports = class {
  id;
  playerId;
  currentState;
  requiredDestinetion;
  websoketClient;

  constructor(client) {
    this.websoketClient = client;
  }

  init(infoData) {
    this.id = infoData.id;
    this.playerId = infoData.playerId;
    console.log(this.id);
  }

  serveStateChange(data) {
    for (const tank of data.tanks) {
      if (tank.id == this.id) {
        this.currentState = tank;
        break;
      }
    }
  }

  serveChangeDestination(data) {
    this.requiredDestinetion = data;

    if (this.currentState) {
      const commands = this.computeActions(
        {
          x: this.currentState.x,
          y: this.currentState.y,
          angle: this.currentState.angle,
        },
        {
          x: this.requiredDestinetion.x,
          y: this.requiredDestinetion.y,
        }
      );

      //test when commands = [];
      this.websoketClient.send({
        tankId: this.id,
        actions: commands,
      });
    }
  }

  computeActions(current, required) {
    const commands = [];

    if (current.x === required.x && current.y === required.y) {
      return commands;
    }

    const yDiff = required.y - current.y;
    const xDiff = required.x - current.x;

    const tan = yDiff / xDiff;

    const newAngle = Math.atan(tan);

    if (newAngle !== current.angle) {
      commands.push({ angle: newAngle - current.angle });
    }

    const newDistance = xDiff === 0 ? yDiff : xDiff / Math.cos(newAngle);

    commands.push({
      distance: newDistance,
    });

    return commands;
  }
};
