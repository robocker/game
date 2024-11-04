import { useContext, useState } from "react";
import { CommanderServiceContext } from "./App";

function AddressForm() {
  const [commanderAdress, setCommanderAdress] = useState("");
  const [approvedAdress, setApprovedAdress] = useState("");

  const commanderService = useContext(CommanderServiceContext);

  const applyAddress = () => {
    setApprovedAdress(commanderAdress);
    commanderService.sayHi();
  };

  return (
    <>
      {!approvedAdress && (
        <header className="App-header">
          <input
            value={commanderAdress}
            onChange={(e) => setCommanderAdress(e.target.value)}
          />

          {commanderAdress !== "" && (
            <button
              onClick={() => {
                applyAddress();
              }}
            >
              Apply
            </button>
          )}
        </header>
      )}
    </>
  );
}

export default AddressForm;