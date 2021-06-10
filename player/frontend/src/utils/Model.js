import React, { useEffect } from "react"

import { useMultipleSceneLoader } from "./useMultipleSceneLoader";

const Model = (props) => {
  const {
    alwaysSelectAsActiveMesh, meshNames, onLoadProgress, onModelError, onModelLoaded, receiveShadows, reportProgress, scaleToDimension, // SceneLoaderOptions
    rootUrl, sceneFilename, pluginExtension, // other parameters
    ...rest // passed on to "rootMesh"
  } = props;

  const options = {
    alwaysSelectAsActiveMesh,
    meshNames,
    onLoadProgress,
    onModelError,
    onModelLoaded,
    receiveShadows,
    reportProgress,
    scaleToDimension,
  }

  let sceneLoaderResults = {rootMesh:null};

  useEffect(() => {

    let sceneLoaderResults = useMultipleSceneLoader(rootUrl, sceneFilename, pluginExtension, options);

    return () => {
      // console.log('disposing the sceneloader results.')
      sceneLoaderResults.dispose();
    }
  }, []);

  return (<abstractMesh fromInstance={sceneLoaderResults.rootMesh} {...rest} />);
}

export default Model;