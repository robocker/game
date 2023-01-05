const Tank = require("../src/Tank");

describe("Tank", () => {
  let tank;
  let client;

  beforeEach(() => {
    client = { send: jest.fn() };
    tank = new Tank(client);
    tank.setWebsocketClient(client);

    tank.init({ id: 42, playerId: 1024 });
  });

  test("should init correctly", () => {
    expect(tank.id).toBe(42);
    expect(tank.playerId).toBe(1024);
  });

  test("should save current position", () => {
    tank.serveStateChange(
      JSON.stringify({
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
      })
    );

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

  test("should serveChangeDestination save current position", () => {
    tank.serveChangeDestination({ x: 14, y: 42 });

    expect(tank.requiredDestinetion).toStrictEqual({ x: 14, y: 42 });

    expect(client.send).not.toHaveBeenCalled();
  });

  it("should serveChangeDestination sendncorrect path- along X", () => {
    tank.serveStateChange(
      JSON.stringify({
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
      })
    );
    tank.serveChangeDestination({ x: 30, y: 5 });

    expect(client.send).toHaveBeenCalledWith(
      '{\"tankId\":42,\"actions\":[{\"distance\":28,\"turret\":{\"angle\":0,\"verticalAngle\":0.7853981633974483,\"shoot\":\"END_OF_ACTION\"}}]}'
    );
  });

  it("should computeActions made correct commands: any move", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 0, y: 0 }, []);
  });

  it("should computeActions made correct commands: along X- 0 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 5, y: 0 }, [
      { distance: 5 },
    ]);
  });

  it("should computeActions made correct commands: 45 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 5, y: 5 }, [
      { angle: Math.PI / 4 },
      { distance: 5 * Math.SQRT2 },
    ]);
  });

  it("should computeActions made correct commands: 90 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 0, y: 5 }, [
      { angle: Math.PI / 2 },
      { distance: 5 },
    ]);
  });

  it("should computeActions made correct commands: 135 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: -5, y: 5 }, [
      { angle: (3 * Math.PI) / 4 },
      { distance: 5 * Math.SQRT2 },
    ]);
  });

  it("should computeActions made correct commands: 180 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: -5, y: 0 }, [
      { angle: Math.PI },
      { distance: 5 },
    ]);
  });

  it("should computeActions made correct commands: -45 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 5, y: -5 }, [
      { angle: -Math.PI / 4 },
      { distance: 5 * Math.SQRT2 },
    ]);
  });

  it("should computeActions made correct commands: -90 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: 0, y: -5 }, [
      { angle: -Math.PI / 2 },
      { distance: 5 },
    ]);
  });

  it("should computeActions made correct commands: -135 degree", () => {
    testCompareActions({ x: 0, y: 0, angle: 0 }, { x: -5, y: -5 }, [
      { angle: (-3 * Math.PI) / 4 },
      { distance: 5 * Math.SQRT2 },
    ]);
  });

  it("should computeActions made correct commands: 60 degree for not zero start", () => {
    testCompareActions(
      { x: 0, y: 0, angle: Math.PI / 12 },
      { x: 5, y: 5 * Math.sqrt(3) },
      [{ angle: Math.PI / 4 }, { distance: 10 }]
    );
  });

  it("should computeActions made correct commands: -60 degree for not zero sstart", () => {
    testCompareActions({ x: 1, y: 1, angle: Math.PI / 12 }, { x: 6, y: -4 }, [
      { angle: -Math.PI / 3 },
      { distance: 5 * Math.SQRT2 },
    ]);
  });

  function testCompareActions(currentState, requiredState, expectResult) {
    const result = tank.computeActions(currentState, requiredState);

    expect(result.length).toBe(expectResult.length);

    for (const i in expectResult) {
      if (expectResult[i].angle) {
        expect(result[i].angle).toBe(expectResult[i].angle);
      } else {
        expect(result[i].distance).toBeCloseTo(expectResult[i].distance, 2);
      }
    }
  }
});
