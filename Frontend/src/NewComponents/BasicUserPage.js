import React, {Component} from 'react';
import * as API_DEVICES from "../services/DeviceAPI";
//import SockJsClient from 'react-stomp';
//import Modal from 'react-modal';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
// Set the root element for accessibility
//Modal.setAppElement('#BasicUserPage');
import {
    Avatar,
    Button,
    List,
    ListItem,
    ListItemAvatar,
    ListItemText,
    TextField,
    Typography,
} from '@material-ui/core';


class BasicUserPage extends Component {
    constructor(props) {
        super(props);
        this.state = {
            devices: [],
            isLoaded: false,
            errorStatus: 0,
            error: null,
            messages: [],
            socket: null,
            deviceId: '',
            //for chat
            messages1: [],
            message: '',
            nickname: '',
            stompClient: null,

        };
        this.handleInput = this.handleInput.bind(this);
        this.simulateDevices = this.simulateDevices.bind(this);
    }

    logout(){
        localStorage.removeItem("jwt");
        window.location.href ="/login";
    }

    handleInput = (e) => {
        this.setState(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }))
    }

    simulateDevices(){

        const device ={
            id: this.state.deviceId
        }

            return API_DEVICES.simulateDevice(device, (result,status,err)=>{
                if (result == null || status !== 200) {
                    this.setState(({
                        errorStatus: status,
                        error: err
                    }));
                }
            });
    }


    fetchDevices() {
        return API_DEVICES.getDevices((result, status, err) => {

            if (result !== null && status === 200) {
                this.setState({
                    devices: result,
                    isLoaded: true
                });
            } else {
                this.setState(({
                    errorStatus: status,
                    error: err
                }));
            }
        });
    }

    componentDidMount() {
        this.fetchDevices();
        //trebuie schimbat pentru deploy pe docker - 8082
        const newSocket = new SockJS('http://localhost:8082/socket');
        const stompClient = Stomp.over(newSocket);

        stompClient.connect({}, frame => {
            this.setState({ socket: stompClient });
        });

        //for chat
        const socket = new SockJS('http://localhost:8084/ws');
        const client = Stomp.over(socket);

        const connectCallback = () => {
            client.subscribe('/topic/messages', (message) => {
                const receivedMessage = JSON.parse(message.body);
                this.setState((prevState) => ({
                    messages1: [...prevState.messages1, receivedMessage],
                }));
            });
        };

        const errorCallback = (error) => {
            console.error('WebSocket error:', error);
            // You can handle the error as needed.
        };

        client.connect({}, connectCallback, errorCallback);

        this.setState({
            stompClient: client,
        });
    }

    componentWillUnmount() {
        // Clean up the WebSocket connection when the component unmounts
        const { socket } = this.state;
        if (socket && socket.connected) {
            socket.disconnect();
        }

        //for chat
        const { stompClient } = this.state;
        if (stompClient && stompClient.connected) {
            stompClient.disconnect();
            console.log('WebSocket disconnected');
        }
    }

    //for chat
    sendMessage = () => {
        const { message, stompClient, nickname } = this.state;

        if (this.state.message.trim() && this.state.stompClient && this.state.stompClient.connected) {
            const messageToSend = {
                username: nickname,
                content: message,
            };
            stompClient.send('/app/chat', {}, JSON.stringify(messageToSend));
            this.setState({
                message: '',
            });
        } else {
            console.error('WebSocket connection is not yet established.');
        }
    };
    handleNicknameChange = (event) => {
        this.setState({
            nickname: event.target.value,
        });
    };

    handleMessageChange = (event) => {
        this.setState({
            message: event.target.value,
        });
    };

    componentDidUpdate(prevProps, prevState) {
        const { socket } = this.state;

        if (socket && socket.connected && prevState.socket !== socket) {
            // Subscribe to the WebSocket topic to receive messages
            socket.subscribe('/topic/notification', message => {
                alert(message.body);
            });
        }
    }

    render() {
        return (
            <div className = "App">
                <h1>You logged in as a Client</h1>
                <p>
                    This is a page basic users can use to see information regarding our devices.
                </p>
                <table>
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Description</th>
                        <th>Address</th>
                        <th>Max Consumption</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.devices.map(
                            device =>
                                <tr key = {device.id}>
                                    <td>{device.id}</td>
                                    <td>{device.description}</td>
                                    <td>{device.address}</td>
                                    <td>{device.maxConsumption}</td>
                                </tr>
                        )
                    }
                    </tbody>
                </table>
                <div>
                    <label htmlFor={"deviceId"}>DeviceId</label>
                    <input
                        name = "deviceId"
                        type="text"
                        id="deviceId"
                        value={this.state.deviceId}
                        onChange={this.handleInput}
                        required
                    />
                    <button onClick={this.simulateDevices}>Simulate Device</button>
                </div>
                <div>
                    {<button onClick={this.logout}>Logout</button>}
                </div>

                <div>
                    <List>
                        {this.state.messages1.map((message, index) => (
                            <ListItem key={index}>
                                <ListItemAvatar>
                                    <Avatar>{message.username.charAt(0)}</Avatar>
                                    <ListItemText
                                        primary={<Typography variant = "Username" gutterBottom>{message.username}</Typography>}
                                        secondary={message.content}
                                    />
                                </ListItemAvatar>
                            </ListItem>
                        ))}
                    </List>
                    <div style={{display: 'flex', alignItems: 'center'}}>
                        <TextField id="standard-basic" label="Message" value={this.state.message} onChange={this.handleMessageChange}/>
                        <TextField id="standard-basic" label="nickname" value={this.state.nickname} onChange={this.handleNicknameChange}/>
                        <Button variant="contained" color="primary" onClick={this.sendMessage} disabled={!this.state.message.trim()}>Send</Button>
                    </div>
                </div>



            </div>
        );
    }
}

export default BasicUserPage;