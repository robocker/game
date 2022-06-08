export const Utils = {
  getColor: function (value) {
    return value > 0 ? Math.floor(255 / value) : 0;
  },
};
