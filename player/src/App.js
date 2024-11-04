import { createContext, useEffect } from "react";
import AddressForm from "./AddressForm";
import "./App.css";
import { CommanderService } from "./CommanderService";
import axios from "axios";

export const CommanderServiceContext = createContext(CommanderService.instance);

function App() {
  useEffect(() => {
    console.log("inside effect");

    axios.get("/api/info").then((result) => {
      console.log(result);
    });
  }, []);

  return (
    <div className="App">
      <AddressForm></AddressForm>
    </div>
  );
}

export default App;
