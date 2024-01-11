import React, {useState,useEffect} from 'react';
import {useLocalState} from "../useLocalStorage";
import * as API_DEVICES from "../services/DeviceAPI";
import {assign} from "../services/DeviceAPI";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import {Avatar, Button, List, ListItem, ListItemAvatar, ListItemText, TextField, Typography} from "@material-ui/core";

const Admin = () => {

    const [jwt, setJwt] = useLocalState("", "jwt");

    const [devices, setDevices] = useState([]);
    const [deviceId,setDeviceId] = useState("");
    const [description,setDescription] = useState("");
    const [address,setAddress] = useState("");
    const [maxCon,setMaxCon] = useState("");

    const [users, setUsers] = useState([]);
    const [id, setId] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [role, setRole] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const[mapUserId,setMapUserId] = useState("");
    const[mapDeviceId,setMapDeviceId] = useState("");

    //for chat
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [nickname, setNickname] = useState('');
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8084/ws');
        const client = Stomp.over(socket);

        client.connect({}, () => {
            client.subscribe('/topic/messages', (message) => {
                const receivedMessage = JSON.parse(message.body);
                setMessages((prevMessages) => [...prevMessages, receivedMessage]);
            });
        });
        setStompClient(client);
        return () => {
            if (client.connected) {
                client.disconnect();
                console.log('WebSocket disconnected');
            }
        }
    },[]);


    const sendMessage = () => {
        if(message.trim() && stompClient && stompClient.connected ){
            const messageToSend = {
                username: nickname,
                content: message
            };
            stompClient.send("/app/chat", {}, JSON.stringify(messageToSend));
            setMessage('');
        }else {
            console.error("WebSocket connection is not yet established.");
        }
    }


    const handleNicknameChange = (event) => {
        setNickname(event.target.value);
    }

    const handleMessageChange = (event) => {
        setMessage(event.target.value);
    }

    function logout(){
        localStorage.removeItem("jwt");
        window.location.href ="/login";
    }


    function getDevices(){
        return API_DEVICES.getDevices((result, status, err) => {
            if(result!==null && status === 200){
                setDevices(result);
            }
        });
    }

    function createDevice(){
        const device ={
            id: id,
            description: description,
            address: address,
            maxConsumption: maxCon
        }
        return API_DEVICES.postDevice(device,(result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("inserted device");
            }

            });
    }

    function deleteDevice(){
        const device ={
            id: deviceId,
            description: description,
            address: address,
            maxConsumption: maxCon
        }
        return API_DEVICES.deleteDevice(device,(result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("deleted device");
            }

        });
    }

    function updateDevice(){
        const device ={
            id: deviceId,
            description: description,
            address: address,
            maxConsumption: maxCon
        }
        return API_DEVICES.putDevice(device,(result, status, error) => {
            if (result !== null && (status === 200 || status === 201)) {
                console.log("updated device");
            }

        });
    }

    function assignDevice(){
        const assignation={
            deviceId: mapDeviceId,
            userId : mapUserId
        }
        return API_DEVICES.assign(assignation,(result,status,error)=>{
            if(result !== null && (status===200 || status===201)){
                console.log("assignation created");
            }
        });
    }
    function unAssignDevice(){
        const assignation= {
            deviceId: mapDeviceId
        }
        return API_DEVICES.unassign(assignation,(result,status,error)=>{
            if(result !== null && (status===200 || status===201)){
                console.log("assignation deleted");
            }
        });
    }


    function createUser() {
        const user = {
            firstName: firstName,
            lastName:lastName,
            role:role,
            username:username,
            password: password
        }
        console.log(JSON.stringify(user));
        fetch("/user",{
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`,
            },
            method : "POST",
            body: JSON.stringify(user),
        }).then((response) =>{
            if(response.status === 200){
                return response.json();

            }
        }).then((data)=> {
            console.log(data);});

    }
    function getUsers() {
        console.log(`Bearer ${jwt}`);
        fetch("/user", {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`,
            },
            method: "GET",
        }).then((response) => {
            if (response.status === 200) {
                return response.json();

            }
        }).then((data) => {
            console.log(data);
            setUsers(data);
        });
        console.log(users);
    }

    function updateUser(){
        const user = {
            firstName: firstName,
            lastName:lastName,
            role:role,
            username:username,
            password: password
        }
        console.log(`/user/${id}`);
        fetch(`/user/${id}`,{
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`,
            },
            method : "PUT",
            body: JSON.stringify(user),
        }).then((response) =>{
            if(response.status === 200){
                return response.json();
            }
        }).then((data)=> {
            console.log(data);});
    }

    function deleteUser(){
        fetch(`/user/${id}`,{
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: `Bearer ${jwt}`,
            },
            method : "DELETE",
        }).then((response) =>{
            if(response.status === 200){
                return response.json();
            }
        }).then((data)=> {
            console.log(data);});
    }



    return (
        <div>
            <h1>Admin Page</h1>
            <h2>Table 1</h2>
            <table>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>Description</th>
                    <th>Address</th>
                    <th>Max Consumption</th>
                    <th>Mapped To</th>
                </tr>
                </thead>
                <tbody>
                {
                    devices.map(
                        device =>
                            <tr key = {device.id}>
                                <td>{device.id}</td>
                                <td>{device.description}</td>
                                <td>{device.address}</td>
                                <td>{device.maxConsumption}</td>
                                <td>{device.userId}</td>
                            </tr>
                    )
                }
                </tbody>
            </table>

            <div>
                <label htmlFor={"deviceId"}>DeviceId</label>
                <input
                    type="text"
                    id="deviceId"
                    value={deviceId}
                    onChange={(event) => setDeviceId(event.target.value)}
                    required

                />
                <label htmlFor={"description"}>Description</label>
                <input
                    type="text"
                    id="description"
                    value={description}
                    onChange={(event) => setDescription(event.target.value)}
                    required

                />
                <label htmlFor={"address"}>Address</label>
                <input
                    type="text"
                    id="address"
                    value={address}
                    onChange={(event) => setAddress(event.target.value)}
                    required

                />
                <label htmlFor={"maxCon"}>MaxConsumption</label>
                <input
                    type="text"
                    id="maxCon"
                    value={maxCon}
                    onChange={(event) => setMaxCon(event.target.value)}
                    required

                />
                {<button onClick={getDevices}>Show Devices</button>}
                {<button onClick={createDevice}>Create Device</button>}
                {<button onClick={updateDevice}>Update Device</button>}
                {<button onClick={deleteDevice}>Delete Device</button>}

            </div>
            <label htmlFor={"mappingUser"}>User Id</label>
            <input
                type="text"
                id="mapuserid"
                value={mapUserId}
                onChange={(event) => setMapUserId(event.target.value)}
                required

            /><label htmlFor={"mappingUser"}>Device Id</label>
            <input
                type="text"
                id="mapdeviceid"
                value={mapDeviceId}
                onChange={(event) => setMapDeviceId(event.target.value)}
                required

            />
            {<button onClick={assignDevice}>Assign Device</button>}
            {<button onClick={unAssignDevice}>Unassign Device</button>}
            <div>

            </div>
            <h2>Table 2</h2>
            <table>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Role</th>
                    <th>Username</th>
                    <th>Password</th>
                </tr>
                </thead>
                <tbody>
                {users.map(user => (
                    <tr key={user.id}>
                        <td>{user.id}</td>
                        <td>{user.firstName}</td>
                        <td>{user.lastName}</td>
                        <td>{user.role}</td>
                        <td>{user.username}</td>
                        <td>{user.password}</td>
                    </tr>
                ))}
                </tbody>
            </table>

            <div>
                <label htmlFor={"id"}>Id</label>
                <input
                    type="text"
                    id="id"
                    value={id}
                    onChange={(event) => setId(event.target.value)}
                    required

                />
                <label htmlFor={"firstName"}>firstName</label>
                <input
                    type="text"
                    id="firstName"
                    value={firstName}
                    onChange={(event) => setFirstName(event.target.value)}
                    required

                />
                <label htmlFor={"lastName"}>lastName</label>
                <input
                    type="text"
                    id="lastName"
                    value={lastName}
                    onChange={(event) => setLastName(event.target.value)}
                    required

                />
                <label htmlFor={"role"}>role</label>
                <input
                    type="text"
                    id="role"
                    value={role}
                    onChange={(event) => setRole(event.target.value)}
                    required

                />
                <label htmlFor={"username"}>username</label>
                <input
                    type="text"
                    id="username"
                    value={username}
                    onChange={(event) => setUsername(event.target.value)}
                    required

                />
                <label htmlFor={"password"}>password</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    required

                />
                {<button onClick={getUsers}>Show Users</button>}
                {<button onClick={createUser}>Create User</button>}
                {<button onClick={updateUser}>Update User</button>}
                {<button onClick={deleteUser}>Delete User</button>}
            </div>
            <div>
                {<button onClick={logout}>Logout</button>}
            </div>

            <div>
                <List>
                    {messages.map((message, index) => (
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
                    <TextField id="standard-basic" label="Message" value={message} onChange={handleMessageChange}/>
                    <TextField id="standard-basic" label="nickname" value={nickname} onChange={handleNicknameChange}/>
                    <Button variant="contained" color="primary" onClick={sendMessage} disabled={!message.trim()}>Send</Button>
                </div>
            </div>
        </div>
    );
};

export default Admin;
