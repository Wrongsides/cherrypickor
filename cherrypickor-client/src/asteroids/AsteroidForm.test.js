import React from 'react';
import ReactDOM from 'react-dom';
import AsteroidForm from './AsteroidForm';

it('renders without crashing', () => {
  const div = document.createElement('div');
  ReactDOM.render(<AsteroidForm />, div);
  ReactDOM.unmountComponentAtNode(div);
});
