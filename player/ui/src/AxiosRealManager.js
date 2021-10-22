import axios from "axios";

export class AxiosManager {
  static get(url, parameters) {
    return axios.get(url, parameters);
  }

  static post(url, parameters) {
    return axios.get(url, parameters);
  }
}
