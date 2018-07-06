import React, { Component } from 'react';
import AsteroidForm from "./asteroids/AsteroidForm";
import logo from './assets/dark-ochre-64.png';
import './App.css';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to Cherrypickor</h1>
        </header>
          <p className="App-intro"/>
          <div>
              <AsteroidForm />
          </div>
      </div>
    );
  }
}

export default App;
