export function convertToDisplayCoordinate(mapX, mapY) {
    const resultX = mapX;
    const resultZ = - mapY;

    return {x: resultX, z: resultZ}
}