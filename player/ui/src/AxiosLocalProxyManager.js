import axios from "axios";

export class AxiosManager {
  static get(url, parameters) {
    return axios.get("http://localhost:8080"+url, parameters);
  }

  static post(url, parameters) {
    return axios.post("http://localhost:8080"+url, parameters);
  }
}
