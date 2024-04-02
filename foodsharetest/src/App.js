import logo from './logo.svg';
import './App.css';
import Chat from "./pages/chat/Chat";
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {
  return (
      <BrowserRouter>
        <div className="App">

          <main>
            <Routes>
              <Route path="/chat/GetChat" element={<Chat/>}/>
            </Routes>
          </main>

        </div>
      </BrowserRouter>
  );
}

export default App;
