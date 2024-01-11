import React, {useState} from 'react';
import {useLocalState} from "../useLocalStorage";

const Login = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [jwt, setJwt] = useLocalState("", "jwt");

    function sendLoginRequest() {
        const reqBody = {
            "username": username,
            "password": password
        };
        fetch("/auth/login", {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            method: "post",
            body: JSON.stringify(reqBody),
        }).then((response) => {
            if(response.status === 200) {
                return Promise.all([response.json(), response.headers])
            }
            else
                return Promise.reject("Invalid Login Attempt");
        })
            .then(([body, headers]) => {
                console.log("something",body);
                setJwt(headers.get("authorization"));
                window.location.href = `/${body.role}`
            })
            .catch((message) => {
                alert(message);
        });

    }


    return (
        <>
            <div>
                <label htmlFor="username">Username</label>
                <input type="username" id="username" value={username} onChange={(event) =>setUsername(event.target.value)}/>
            </div>
            <div>
                <label htmlFor="password">Password</label>
                <input type="password" id="password" value={password} onChange={(event) =>setPassword(event.target.value)}/>
            </div>
            <div>
                <button id="submit" type="button" onClick={() => sendLoginRequest()}>Login</button>
            </div>
        </>
    );
};

export default Login;
