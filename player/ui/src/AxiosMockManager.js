import axios from "axios";

export class AxiosManager {
  static get(url, parameters) {
      console.log('mock');
    return {then:function(){return {}}};
  }

  static post(url, parameters) {
    console.log('mock');
    return {then:function(){return {}}};
  }
}
