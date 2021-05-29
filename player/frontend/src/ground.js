import React, { useCallback } from 'react';
import { Vector3, Color3, Color4 } from '@babylonjs/core';


export const Ground = ({imageWidht, imageHeight}) => {

    const subdivision = 512;
    const xOffset = imageWidht/2;
    const zOffset = -imageHeight/2;
    
    return (
    <groundFromHeightMap name='ContinuousTerrain' position={new Vector3(xOffset, 0, zOffset)} 
    url="assets/heightMap.png" width={imageWidht} height={imageHeight} subdivisions= {subdivision}
    maxHeight= {5} minHeight={0}>
        <standardMaterial name='terrain-material' diffuseColor={Color3.Gray()} assignTo='mesh.material' wireframe={false} />
    </groundFromHeightMap>
      )
  }