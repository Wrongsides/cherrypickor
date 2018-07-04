require('bootstrap/dist/css/bootstrap.min.css');
require('./css/main.css');
import React from 'react';
import ReactDOM from 'react-dom';
class Asteroids extends React.Component {
    render() {
        return(
            <div class="col-md-4 text-center">
            <input type="button" className="btn btn-primary" value="Submit" />
            </div>
        );
    }
}
ReactDOM.render(
    <Asteroids />,
    document.getElementById('app')
);