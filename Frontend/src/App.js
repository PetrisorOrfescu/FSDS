import React, {useEffect} from "react";
import './App.css';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom'

import Welcome from "./NewComponents/Welcome";
import BasicUserPage from "./NewComponents/BasicUserPage";
import PrivateRoute from "./PrivateRoute/PrivateRoute";
import Login from "./NewComponents/Login";
import Admin from "./NewComponents/Admin";

function App() {


    // useEffect(() => {
    //     if(!jwt){
    //         const reqBody = {
    //             "username": "stoleMariosIdentity",
    //             "password": "1234"
    //         };
    //         fetch("/auth/login",{
    //             headers : {
    //                 'Accept': 'application/json',
    //                 'Content-Type': 'application/json',
    //             },
    //             method: "post",
    //             body: JSON.stringify(reqBody),
    //         }).then((response) => Promise.all([response.json(), response.headers]))
    //             .then(([body,headers]) => {
    //                 setJwt( headers.get("authorization"));
    //
    //             })
    //     }
    //
    // }, []);



    return (
        <div >
            {}
            <Router>
                <Routes>
                    {}
                    <Route
                        exact
                        path="/"
                        element={<Welcome />}
                    />
                    <Route
                        path="/login"
                        element={<Login />}
                    />
                    <Route
                        path = "/client"
                        element={<PrivateRoute>
                            <BasicUserPage />
                        </PrivateRoute>}
                    />
                    <Route
                        path = "/admin"
                        element={
                        <PrivateRoute>
                            <Admin />
                        </PrivateRoute>
                    }
                    />
                </Routes>
            </Router>

        </div>
    );
}

export default App;
