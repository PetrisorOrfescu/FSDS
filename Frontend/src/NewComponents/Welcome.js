import React, {Component} from 'react';
import { Link } from 'react-router-dom';
class Welcome extends Component {
    constructor(props) {
        super(props);
        this.state = {};
    }
    render() {
        return (
            <div>

                <h1>Greetings!</h1>
                <p>This is the main page of my Energy Management System. For more information and features, please log in. </p>
                <ul>
                    <li>
                        <Link to="/">Home</Link>
                    </li>
                    <li>
                        <Link to="/login">Login Page</Link>
                    </li>
                </ul>
            </div>
        );
    }
}

export default Welcome;