import React, { useCallback } from 'react';
import { Vector3, Color3, Color4 } from '@babylonjs/core';


export const Ground = () => {

    return (
    <groundFromHeightMap name='ContinuousTerrain' position={new Vector3(0, 0, 0)} 
    url="assets/heightMap.png" width={512} height={512} subdivisions= {512}
    maxHeight= {5} minHeight={0}>
        <standardMaterial name='terrain-material' diffuseColor={Color3.Gray()} assignTo='mesh.material' wireframe={false} />
    </groundFromHeightMap>
      )
  }