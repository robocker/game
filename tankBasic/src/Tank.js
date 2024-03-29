const debug = require("debug")("tankBasic:Tank.js");

module.exports = class {
  id;
  playerId;
  currentState;
  requiredDestinetion;
  websoketClient;

  init(infoData) {
    this.id = infoData.id;
    this.playerId = infoData.playerId;
    console.log(this.id);
  }

  setWebsocketClient(client) {
    this.websoketClient = client;
  }

  serveStateChange(data) {
    const dataObj = JSON.parse(data);

    for (const tank of dataObj.tanks) {
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
          z: this.currentState.z,
          angle: this.currentState.angle,
        },
        {
          x: this.requiredDestinetion.x,
          z: this.requiredDestinetion.z,
        }
      );

      //test when commands = [];
      debug(commands);
      if (this.websoketClient) {
        this.websoketClient.send(
          JSON.stringify({
            tankId: this.id,
            actions: commands,
          })
        );
      }
    }
  }

  computeActions(current, required) {
    debug(current, required);
    const commands = [];

    if (current.x === required.x && current.z === required.z) {
      return commands;
    }

    const zDiff = required.z - current.z;
    const xDiff = required.x - current.x;

    debug("dif:", xDiff, zDiff);

    const tan = zDiff / xDiff;

    let newAngle = Math.atan(tan);
    debug("newAngle:", newAngle);

    if (xDiff < 0 && zDiff > 0) {
      newAngle += Math.PI;
    }

    if (xDiff < 0 && zDiff < 0) {
      newAngle -= Math.PI;
    }

    if (newAngle !== current.angle) {
      debug({ newAngle, currentAngle: current.angle });
      commands.push({ angle: newAngle - current.angle });
    } else if (xDiff < 0 && zDiff == 0) {
      debug({ currentAngle: current.angle });
      commands.push({ angle: Math.PI });
    }

    const newDistance = xDiff === 0 ? zDiff : xDiff / Math.cos(newAngle);

    const actionTurret = {
      angle: Math.PI/4,
      verticalAngle: 0,
      shoot: "END_OF_ACTION",
    };

    commands.push({
      distance: Math.abs(newDistance),
      turret: actionTurret
    });

    return commands;
  }
};
