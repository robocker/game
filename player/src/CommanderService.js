import { BehaviorSubject } from "rxjs";

export class CommanderService {
  static _instance;
  address$ = new BehaviorSubject();

  static get instance() {
    if (!this._instance) {
      this._instance = new CommanderService();
    }

    return this._instance;
  }

  setAddress(address) {
    this.address$.next(address);
  }
}
