import { AxiosMockManager } from "./Implementation/AxiosMockManager";
import { AxiosRealManager } from "./Implementation/AxiosRealManager";

export function AxiosManagerFactory() {
  switch (process.env.REACT_APP_AXIOS_SERVICE_IMPLEMETATION) {
    case "AxiosMockManager":
      return AxiosMockManager;
    default:
      return AxiosRealManager;
  }
}
