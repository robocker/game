import { LogManager } from "./LogManager";

export class AxiosManager {
  static get(url, parameters) {
    LogManager.instance.debug(`Mocked server response for: ${url}`);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        switch (url) {
          case "/api/info":
            resolve({
              data: {
                players: [
                  {
                    current: true,
                    color: { r: 0, g: 0, b: 1 },
                    tanks: [
                      {
                        containerName: "tank-1",
                        id: "1",
                        imageName: "robocker/tankbasic",
                        insidePortNumber: 80,
                        angle: 0,
                        turret: {
                          angle: 0,
                          angleVertical: 0,
                        },
                        x: -10,
                        y: 0,
                        lifeLevel: 3,
                      },
                      {
                        containerName: "tank-2",
                        id: "2",
                        imageName: "robocker/tankbasic",
                        insidePortNumber: 80,
                        angle: Math.PI / 4,
                        turret: {
                          angle: Math.PI / 4,
                          angleVertical: Math.PI / 4,
                        },
                        x: 0,
                        y: 0,
                        lifeLevel: 3,
                      },
                      {
                        containerName: "tank-4",
                        id: "4",
                        imageName: "robocker/tankbasic",
                        insidePortNumber: 80,
                        angle: -Math.PI,
                        turret: {
                          angle: 0,
                          angleVertical: Math.PI / 4,
                        },
                        x: 10,
                        y: 0,
                        lifeLevel: 3,
                      },
                    ],
                  },
                  {
                    current: false,
                    color: { r: 1, g: 0, b: 0 },
                    tanks: [
                      {
                        containerName: "tank-3",
                        id: "3",
                        imageName: "robocker/tankbasic",
                        insidePortNumber: 80,
                        angle: -Math.PI*3/4,
                        turret: {
                          angle: -Math.PI / 4,
                          angleVertical: Math.PI / 6,
                        },
                        x: -10,
                        y: 20,
                        lifeLevel: 3,
                      },
                    ],
                  },
                ],
              },
            });
            break;

          default:
            reject({ msg: `Not Mocked GET request: ${url}`, parameters });
        }
      }, 300);
    });
  }

  static post(url, parameters) {
    LogManager.instance.debug(`Mocked server response: ${url}`);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        switch (url) {
          case "/api/tanks/move":
            resolve({ msg: "move", parameters: parameters });
            break;
          default:
            reject({ msg: `Not Mocked POST request: ${url}`, parameters });
        }
      }, 300);
    });
  }
}
