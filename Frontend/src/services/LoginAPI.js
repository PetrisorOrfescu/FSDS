import {HOST} from "../hosts"
import RestApiClient from "../rest-client"

const endpoint = {
    login: '/auth/login'
};

function performLogin(login, callback){
    let request = new Request(HOST.backend_api_users + endpoint.login, {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(login)
    });
    RestApiClient.performRequest(request,callback);
}

export {performLogin}