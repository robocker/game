import React, { Component, Suspense, useEffect, useState } from 'react';
import '@babylonjs/inspector';
import Model from './utils/Model';
import { Vector3, Color3 } from '@babylonjs/core';
import { ActionManager, SetValueAction, ExecuteCodeAction } from '@babylonjs/core/Actions';
import { convertToDisplayCoordinate } from './utils';
import ScaledModelWithProgress from './ScaledModelWithProgress';

const axios = require('axios');

export function Tank(props) {

    const coordinates = convertToDisplayCoordinate(props.x, props.y); //{x, y, widthX, widthY, height, name}

    const [tankXPos, setTankXPos] = useState(coordinates.x);
    const [tankZPos, setTankZPos] = useState(coordinates.z);
    const [tankYPos, setTankYPos] = useState(10);
    const [tankScaling, setTankScaling] = useState(5);


    const handleKeyPress = (event)=> {
        if(event.key === 'w'){
            setTankZPos(tankZPos + 0.1);
        }

        if(event.key === 's'){
            setTankZPos(tankZPos + 0.1);
        }

        if(event.key === 'd'){
            setTankXPos(tankXPos + 0.1);
        }

        if(event.key === 'a'){
            setTankXPos(tankXPos - 0.1);
        }

        if(event.key === 'q'){
            setTankYPos(tankYPos + 0.1);
        }

        if(event.key === 'e'){
            setTankYPos(tankYPos - 0.1);
        }

        console.log(tankXPos,tankYPos,tankZPos);

    }

    const sendClickMessage= ()=> {

        console.log('clicked');

        axios.post('/api/tanks/move', {
            ids: [42, 11],
            destination: {x: 33, y: 98}
        })
        .then(function (response) {
            console.log(response);
        })
        .catch(function (error) {
            console.log(error);
        });
    }

    useEffect(()=>{
        document.body.addEventListener('keydown', handleKeyPress);
        console.log('useEffect');

        return ()=>{
            document.body.removeEventListener('keydown', handleKeyPress);
        }
    },[])


    const onModelLoaded  = (model, sceneContext) => {
        let mesh = model.meshes[1]
        mesh.actionManager = new ActionManager(mesh._scene)
        mesh.actionManager.registerAction(
            new SetValueAction(
            ActionManager.OnPointerOverTrigger,
            mesh.material,
            'wireframe',
            true
            )
        )
        mesh.actionManager.registerAction(
            new ExecuteCodeAction(
            ActionManager.OnPickTrigger,
            sendClickMessage
            )
        )
        mesh.actionManager.registerAction(
            new SetValueAction(
            ActionManager.OnPointerOutTrigger,
            mesh.material,
            'wireframe',
            false
            )
        )
    }



    let baseUrl = 'assets/tank/'
    let baseUrl2 = 'https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/'

    return (
    <Suspense fallback={<box name='fallback' position={new Vector3(-2.5, tankYPos, 0)} />}>
        <Model rootUrl={`${baseUrl}`} sceneFilename='tank.glb' scaleToDimension={tankScaling} position={new Vector3(tankXPos, tankYPos, tankZPos)}
        onModelLoaded={onModelLoaded}/>
         <ScaledModelWithProgress rootUrl={`${baseUrl2}BoomBox/glTF/`} sceneFilename='BoomBox.gltf' scaleTo={5}
            progressBarColor={Color3.FromInts(255, 165, 0)} center={new Vector3(tankXPos+10, tankYPos, tankZPos)}
            onModelLoaded={onModelLoaded}
          />
         <ScaledModelWithProgress rootUrl={`${baseUrl2}BoomBox/glTF/`} sceneFilename='BoomBox.gltf' scaleTo={5}
            progressBarColor={Color3.FromInts(255, 165, 0)} center={new Vector3(tankXPos+30, tankYPos, tankZPos)}
            onModelLoaded={onModelLoaded}
          />
    </Suspense>

    )


}