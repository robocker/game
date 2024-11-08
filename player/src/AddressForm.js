import { useContext, useState } from "react";
import { CommanderServiceContext } from "./App";

function AddressForm() {
  const [commanderAdress, setCommanderAdress] = useState(
    "ws://localhost:8070/connection"
  );
  const [approvedAdress, setApprovedAdress] = useState("");

  const commanderService = useContext(CommanderServiceContext);

  const applyAddress = () => {
    setApprovedAdress(commanderAdress);
    commanderService.setAddress(commanderAdress);
  };

  return (
    <>
      {!approvedAdress && (
        <header className="App-header" style={{ margin: "4px 12px" }}>
          <div>
            <label>Enter address of your local commander</label>
          </div>
          <div>
            <input
              value={commanderAdress}
              style={{ width: "200px" }}
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
          </div>
        </header>
      )}
    </>
  );
}

export default AddressForm;
