import axios from "axios";

export class AxiosRealManager {
  static get(url, parameters) {
    return axios.get(url, parameters);
  }

  static post(url, parameters) {
    return axios.post(url, parameters);
  }

  static get wsUrl() {
    return "ws://engine/state";
  }
}
