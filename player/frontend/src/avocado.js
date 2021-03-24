import React, { Component, Suspense } from 'react';
import '@babylonjs/inspector';
import { Engine, Scene, Model } from 'react-babylonjs';
import { render } from 'react-dom';
import { Vector3, Color3 } from '@babylonjs/core';
import { ActionManager, SetValueAction } from '@babylonjs/core/Actions';
import ScaledModelWithProgress from './ScaledModelWithProgress';


export class Avocado extends Component {

    constructor () {
        super()
    
        this.state = {
          avocadoYPos: 3.5,
          avocadoScaling: 3.0
        }
    
        this.moveAvocadoUp = this.moveAvocadoUp.bind(this)
        this.moveAvocadoDown = this.moveAvocadoDown.bind(this)
        this.increaseAvocadoSize = this.increaseAvocadoSize.bind(this)
        this.decreaseAvocadoSize = this.decreaseAvocadoSize.bind(this)
        this.onModelLoaded = this.onModelLoaded.bind(this)
      }

      moveAvocadoDown () {
        this.setState((state) => ({
          ...state,
          avocadoYPos: state.avocadoYPos - 0.5
        }))
      }
    
      moveAvocadoUp () {
        this.setState((state) => ({
          ...state,
          avocadoYPos: state.avocadoYPos + 0.5
        }))
      }
    
      increaseAvocadoSize () {
        this.setState((state) => ({
          ...state,
          avocadoScaling: state.avocadoScaling + 0.1
        }))
      }
    
      decreaseAvocadoSize () {
        this.setState((state) => ({
          ...state,
          avocadoScaling: state.avocadoScaling - 0.1
        }))
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
          new SetValueAction(
            ActionManager.OnPointerOutTrigger,
            mesh.material,
            'wireframe',
            false
          )
        )
      }

      render () {

        let baseUrl = 'https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/'
        return (
 

          <Suspense fallback={<box name='fallback' position={new Vector3(-2.5, this.state.avocadoYPos, 0)} />}>
              <Model rootUrl={`${baseUrl}Avocado/glTF/`} sceneFilename='Avocado.gltf' scaleToDimension={this.state.avocadoScaling} position={new Vector3(-2.5, this.state.avocadoYPos, 0)} />
          </Suspense>
          
          )
      }

}