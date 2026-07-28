import { useState } from 'react'
import Nav from "./components/Nav"
import Hero from "./components/Hero"
import Footer from "./components/Footer"

import './App.css'

function App() {
  const [count, setCount] = useState(0)
  let name = "Ritam";
  return (
    
    <>
      <Nav/>
      <Hero/>
      <Footer/>
    </>
  )
}

export default App
