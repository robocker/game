import React, { useRef, useState } from 'react'
import { Engine, Scene, useBeforeRender, useClick, useHover } from 'react-babylonjs'
import { Vector3, Color3, Color4 } from '@babylonjs/core'
import { render } from 'react-dom';
import { Ground } from './ground';
import './app.css';
import { Tank } from './tank';
//import {Radio} from './radio';
//import {Avocado} from './avocado';

const DefaultScale = new Vector3(1, 1, 1);
const BiggerScale = new Vector3(1.25, 1.25, 1.25);

const SpinningBox = (props) => {
  // access Babylon scene objects with same React hook as regular DOM elements
  const boxRef = useRef(null);

  const [clicked, setClicked] = useState(false);
  useClick(
    () => setClicked(clicked => !clicked),
    boxRef
  );

  const [hovered, setHovered] = useState(false);
  useHover(
    () => setHovered(true),
    () => setHovered(false),
    boxRef
  );

  // This will rotate the box on every Babylon frame.
  const rpm = 5;
  useBeforeRender((scene) => {
    if (boxRef.current) {
      // Delta time smoothes the animation.
      var deltaTimeInMillis = scene.getEngine().getDeltaTime();
      boxRef.current.rotation.y += ((rpm / 60) * Math.PI * 2 * (deltaTimeInMillis / 1000));
    }
  });

  return (<box name={props.name} ref={boxRef} size={2} position={props.position} scaling={clicked ? BiggerScale : DefaultScale} >
    <standardMaterial name={`${props.name}-mat`} diffuseColor={hovered ? props.hoveredColor : props.color} specularColor={Color3.Black()} />
  </box>);
}

export const SceneWithSpinningBoxes = () => {

  const [tanks, setTanks] = useState([

    {x: 65, y: 91, name:4},
    {x: 85, y: 91, name:4},
    {x: 125, y: 91, name:4},
]);

    const viewTanks = tanks.map((tank) => {
        console.log(tank.name);
        return (<Tank x = {tank.x} y = {tank.y} key={tank.name}/>);
    });

  return (
  <div>
    <Engine antialias adaptToDeviceRatio canvasId='babylonJS'>
      <Scene clearColor={new Color4(0.2, 0.3, 0.5, 1.0)}>
      <freeCamera name='camera1' position={new Vector3(150, 10, -150)} setTarget={[new Vector3(-255, 0, 255)]} />
        <hemisphericLight name='light1' intensity={0.7} direction={Vector3.Up()} />
        <SpinningBox name='left' position={new Vector3(-2, 3, 10)}
          color={Color3.FromHexString('#FF0000')} hoveredColor={Color3.FromHexString('#C26DBC')}
        />
        <SpinningBox name='right' position={new Vector3(2, 3, 10)}
          color={Color3.FromHexString('#C8F4F9')} hoveredColor={Color3.FromHexString('#3CACAE')}
        />

        {viewTanks}

      <Ground imageWidht={512} imageHeight={512}/>
      </Scene>
    </Engine>
  </div>
)
}

render(<SceneWithSpinningBoxes />, document.getElementById('root'));