import axios from "axios";

export class AxiosManager {
  static get(url, parameters) {
    console.log(`Mocked server response for: ${url}`);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        switch (url) {
          case "api/info":
            resolve({
              data: {
                tanks: [
                  {
                    containerName: "tank-1",
                    height: 10,
                    imageName: "robocker/tankbasic",
                    insidePortNumber: 80,
                    widthX: 5,
                    widthY: 15,
                    x: 105,
                    y: 41,
                  },
                  {
                    containerName: "tank-2",
                    height: 10,
                    imageName: "robocker/tankbasic",
                    insidePortNumber: 80,
                    widthX: 5,
                    widthY: 15,
                    x: 130,
                    y: 55,
                  },
                  {
                    containerName: "tank-3",
                    height: 10,
                    imageName: "robocker/tankbasic",
                    insidePortNumber: 80,
                    widthX: 5,
                    widthY: 15,
                    x: 0,
                    y: 0,
                  },
                  {
                    containerName: "tank-3",
                    height: 10,
                    imageName: "robocker/tankbasic",
                    insidePortNumber: 80,
                    widthX: 5,
                    widthY: 15,
                    x: -200,
                    y: -200,
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
    console.log(`Mocked server response: ${url}`);
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        switch (url) {
          default:
            reject({ msg: `Not Mocked POST request: ${url}`, parameters });
        }
      }, 300);
    });
  }
}
