export class CommanderService {
  static _instance;

  static get instance() {
    if (!this._instance) {
      this._instance = new CommanderService();
    }

    return this._instance;
  }


}
