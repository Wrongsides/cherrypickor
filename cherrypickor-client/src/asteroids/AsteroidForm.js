import {Grid, Row, Col, FormGroup, FormControl, Button} from "react-bootstrap";
import React, {Component} from 'react';

class AsteroidForm extends Component {
    constructor(props) {
        super(props);
        this.state = {value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({value: event.target.value});
    }

    handleSubmit(event) {
        alert('Scanner output was submitted: ' + this.state.value);
        event.preventDefault();
    }

    render() {
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
                            <div class="pull-right"><Button bsStyle="primary" type="submit">Submit »</Button></div>
                        </form>
                    </Col>
                    <Col xs={6} md={4}>
                        Results should go here
                    </Col>
                </Row>
            </Grid>
        );
    }
}

export default AsteroidForm;