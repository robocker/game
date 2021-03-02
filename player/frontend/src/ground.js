import React, { useCallback } from 'react';
import { Vector3, Color3, Color4 } from '@babylonjs/core';


export const Ground = () => {
    const xSize = 500
    const zSize = 500
    const subSize = 100
    
    const getMapData = () => {
      let mapSubX = xSize
      let mapSubZ = zSize
    
      // map creation
      let mapData = new Float32Array(mapSubX * mapSubZ * 3)
      for (var l = 0; l < mapSubZ; l++) {
        for (var w = 0; w < mapSubX; w++) {
          mapData[3 * (l * mapSubX + w)] = (w - mapSubX * 0.5) * 2.0
          mapData[3 * (l * mapSubX + w) + 1] = w / (l + 1) * Math.sin((l + 1) / 2) * Math.cos(w / 2) * 2.0
          mapData[3 * (l * mapSubX + w) + 2] = (l - mapSubZ * 0.5) * 2.0
        }
      }
      return mapData
    }


    return (
    <dynamicTerrain name='ContinuousTerrain' mapData={getMapData()} mapSubX={xSize} mapSubZ={zSize} terrainSub={subSize} position={new Vector3(0, 0, 0)}>
        <standardMaterial name='terrain-material' diffuseColor={Color3.Gray()} assignTo='mesh.material' wireframe={false} />
    </dynamicTerrain>
      )
  }