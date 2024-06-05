import { useState } from "react";
import { ReactComponent as LogoComponent } from "@/assets/react.svg";
// import ReactLogo from "@/assets/react.svg?url";
import ifnosLogo from "/public/logo.png";
import "./App.css";

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <div>
        <a
          href="https://github.com/StaySharp0/ifnos"
          target="_blank"
          title="ifnos github"
        >
          <img src={ifnosLogo} className="logo" alt="Ifnos logo" />
        </a>
        <a href="https://react.dev" target="_blank" title="React">
          <LogoComponent className="logo react" />
          {/* <img src={ReactLogo} className="logo react" alt="React logo" /> */}
        </a>
      </div>
      <h1>Ifnos + React</h1>
      <div className="card">
        <button type="button" onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Ifnos and React logos to learn more
      </p>
    </>
  );
}

export default App;
