import {BrowserRouter, Route, Routes} from "react-router-dom";
import Chat from "./views/Chat/Chat";

function App() {
    return (


        <div className="App">
            <h1>푸드쉐어페이지입니다.</h1>
          <Routes>
            <Route path="/chat/GetChat" element={<Chat/>}/>
          </Routes>
        </div>

    );
}

export default App;
