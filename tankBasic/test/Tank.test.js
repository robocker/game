const Tank = require("../src/Tank");

describe("Tank", () => {
  let tank;
  let client;

  beforeEach(() => {
    client = { send: jest.fn() };
    tank = new Tank(client);

    tank.init({ id: 42, playerId: 1024 });
  });

  test("should init correctly", () => {
    expect(tank.id).toBe(42);
    expect(tank.playerId).toBe(1024);
  });

  test("should save current position", () => {
    tank.serveStateChange({
      tanks: [
        {
          id: 1,
          playerId: 1,
          angle: Math.PI / 4,
          turret: {
            angle: 0,
            angleVertical: Math.PI / 8,
          },
          x: -10,
          y: 10,
        },
        {
          id: 42,
          playerId: 1,
          angle: Math.PI / 4,
          turret: {
            angle: Math.PI / 4,
            angleVertical: Math.PI / 4,
          },
          x: 2,
          y: 54.2,
        },
      ],
    });

    expect(tank.currentState).toStrictEqual({
      id: 42,
      playerId: 1,
      angle: Math.PI / 4,
      turret: {
        angle: Math.PI / 4,
        angleVertical: Math.PI / 4,
      },
      x: 2,
      y: 54.2,
    });
  });

  test("should save current position", () => {
    tank.serveChangeDestination({ x: 14, y: 42 });

    expect(tank.requiredDestinetion).toStrictEqual({ x: 14, y: 42 });
  });

  it("should send correct path- along X", () => {
    tank.serveStateChange({
      tanks: [
        {
          id: 42,
          playerId: 1,
          angle: 0,
          turret: {
            angle: Math.PI / 4,
            angleVertical: Math.PI / 4,
          },
          x: 2,
          y: 5,
        },
      ],
    });
    tank.serveChangeDestination({ x: 30, y: 5 });

    expect(client.send).toHaveBeenCalledWith({
      tankId: 42,
      actions: [
        {
          type: "move",
          distance: 28,
        },
      ],
    });
  });
});
