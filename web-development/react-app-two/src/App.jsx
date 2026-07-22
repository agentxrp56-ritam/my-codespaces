import { useState } from 'react'

import './App.css'

function App() {
  const [count, setCount] = useState(0)
  let name = "Ritam";
  return (
    
    <>
      <h1>Hello</h1>
      <h2>{name}</h2>
      <div className="box">
        <h1>Love you Boss</h1>
      </div>
    </>
  )
}

export default App
