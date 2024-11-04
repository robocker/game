import axios from "axios";

export class AxiosMockManager {
  static get(url, parameters) {
    return axios.get(url, parameters);
  }

  static post(url, parameters) {
    return axios.post(url, parameters);
  }

  static get wsUrl() {
    return "ws://localhost:8080/state";
  }
}
