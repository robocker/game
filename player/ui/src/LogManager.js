const LOG_LEVEL_DEBUG = 1;
const LOG_LEVEL_ERROR = 3;

class LogManager {
  logLevel = LOG_LEVEL_ERROR;
  static _instance;

  prevDebug;
  prevError;

  constructor() {
    window["logmanager"] = this;
  }

  static get instance() {
    if (!self._instance) {
      self._instance = new LogManager();
    }
    return self._instance;
  }

  debug(data) {
    if (this.logLevel <= LOG_LEVEL_DEBUG && data != this.prevDebug) {
      console.log(data);
      this.prevDebug = data;
    }
  }

  error(data) {
    if (this.logLevel <= LOG_LEVEL_ERROR&& data != this.prevError) {
      console.error(data);
      this.prevError = data;
    }
  }
}

export { LogManager, LOG_LEVEL_DEBUG, LOG_LEVEL_ERROR };
