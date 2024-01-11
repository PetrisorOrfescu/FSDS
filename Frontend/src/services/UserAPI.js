//import axios from "axios";
import {HOST} from "../hosts"
import RestApiClient from "../rest-client"
//const USER_API_BASE_URL = "http://localhost:8083/user"

const endpoint = {
    user: '/user'
};
class UserAPI {

    getUsers(callback){

        let request = new Request(HOST.backend_api_users + endpoint.device,{
            method:'GET',
            headers:{
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authentication': 'Bearer'
            }
        });
        console.log(request.url);
        RestApiClient.performRequest(request,callback);
    }
}

export default new UserAPI();