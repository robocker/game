import React, { Component, Suspense } from 'react';
import '@babylonjs/inspector';
import { Engine, Scene, Model } from 'react-babylonjs';
import { render } from 'react-dom';
import { Vector3, Color3 } from '@babylonjs/core';
import { ActionManager, SetValueAction, ExecuteCodeAction } from '@babylonjs/core/Actions';
import ScaledModelWithProgress from './ScaledModelWithProgress';
const axios = require('axios');

export class Tank extends Component {

    constructor () {
        super()
    
        this.state = {
          tankYPos: 17,
          tankXPos: 105,
          tankZPos: -41,
          tankScaling: 10.0
        }
    
        this.moveTankUp = this.moveTankUp.bind(this)
        this.moveTankDown = this.moveTankDown.bind(this)
        this.increaseTankSize = this.increaseTankSize.bind(this)
        this.decreaseTankSize = this.decreaseTankSize.bind(this)
        this.onModelLoaded = this.onModelLoaded.bind(this)

        document.body.addEventListener('keydown', this.handleKeyPress);
      }

      handleKeyPress = (event)=> {

      if(event.key === 'w'){
            this.setState((state) => ({
                            ...state,
                            tankZPos: state.tankZPos + 1
                          }))
      }

      if(event.key === 's'){
            this.setState((state) => ({
                            ...state,
                            tankZPos: state.tankZPos - 1
                          }))
      }

      if(event.key === 'd'){
            this.setState((state) => ({
                            ...state,
                            tankXPos: state.tankXPos + 1
                          }))
      }

      if(event.key === 'a'){
            this.setState((state) => ({
                            ...state,
                            tankXPos: state.tankXPos - 1
                          }))
      }
      if(event.key === 'q'){
            this.setState((state) => ({
                            ...state,
                            tankYPos: state.tankYPos + 1
                          }))
      }

      if(event.key === 'e'){
            this.setState((state) => ({
                            ...state,
                            tankYPos: state.tankYPos - 1
                          }))
      }

        console.log(this.state);
      }

      moveTankDown () {
        this.setState((state) => ({
          ...state,
          tankYPos: state.tankYPos - 0.5
        }))
      }
    
      moveTankUp () {
        this.setState((state) => ({
          ...state,
          tankYPos: state.tankYPos + 0.5
        }))
      }
    
      increaseTankSize () {
        this.setState((state) => ({
          ...state,
          tankScaling: state.tankScaling + 0.1
        }))
      }
    
      decreaseTankSize () {
        this.setState((state) => ({
          ...state,
          tankScaling: state.tankScaling - 0.1
        }))
      }

      sendClickMessage() {

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
    
      onModelLoaded  = (model, sceneContext) => {
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
            this.sendClickMessage
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

      render () {

        let baseUrl = 'assets/tank/'
        return (
 

          <Suspense fallback={<box name='fallback' position={new Vector3(-2.5, this.state.tankYPos, 0)} />}>
              <Model rootUrl={`${baseUrl}`} sceneFilename='tank.glb' scaleToDimension={this.state.tankScaling} position={new Vector3(this.state.tankXPos, this.state.tankYPos, this.state.tankZPos)}
              onModelLoaded={this.onModelLoaded}/>
          </Suspense>
          
          )
      }

}