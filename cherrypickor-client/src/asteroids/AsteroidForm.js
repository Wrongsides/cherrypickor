import {Grid, Row, Col, FormGroup, FormControl, Button, Table} from "react-bootstrap";
import React, {Component} from 'react';

class AsteroidForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isLoading: false,
            value: '',
            asteroids: [],
        };

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        this.setState({isLoading: true})
        fetch('/api/asteroids', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: this.state.value
        }).then((response) => response.json())
            .then((responseJson) => {
                this.setState({
                    asteroids: responseJson.asteroids,
                    isLoading: false
                });
            })
            .catch((error) => {
                console.error(error);
            });
        event.preventDefault();
    }

    render() {
        const {isLoading, asteroids} = this.state;
        return (
            <Grid>
                <Row className="show-grid">
                    <Col xs={8} md={6}>
                        <form onSubmit={this.handleSubmit}>
                            <FormGroup controlId="formControlsTextarea">
                                <FormControl componentClass="textarea"
                                             placeholder="Copy & paste survey scanner output here…"
                                             rows="8"
                                             value={this.state.value}
                                             onChange={this.handleChange}
                                             onSubmit={this.handleSubmit}/>
                            </FormGroup>
                            <div className="pull-right">
                                <Button type="submit"
                                        bsStyle="primary"
                                        disabled={isLoading}>
                                    Submit »
                                </Button>
                            </div>
                        </form>
                    </Col>
                    <Col xs={8} md={6}>
                        <Table>
                            <thead>
                            <tr>
                                <th>Asteroid</th>
                                <th>Quantity</th>
                                <th>Jita Buy Value</th>
                            </tr>
                            </thead>
                            <tbody>
                            {asteroids.map(asteroid =>
                                <tr>
                                    <td>{asteroid.name}</td>
                                    <td>{asteroid.quantity}</td>
                                    <td>{asteroid.value} ISK</td>
                                </tr>
                            )}
                            </tbody>
                        </Table>
                    </Col>
                </Row>
            </Grid>
        );
    }
}

export default AsteroidForm;