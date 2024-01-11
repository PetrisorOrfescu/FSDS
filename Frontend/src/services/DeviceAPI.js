import {HOST} from "../hosts"
import RestApiClient from "../rest-client"

//const DEVICE_API_BASE_URL = "http://localhost:8084/device"

const endpoint = {
  device: '/device',
  assignation: '/mapToDevice',
  simulation: '/devices/start'
};


function getDevices(callback){
    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    console.log('Bearer ' + localStorage.getItem('jwt'));
    let request = new Request(HOST.backend_api + endpoint.device,{
        method:'GET',
        headers: {
            Authorization: 'Bearer ' + modifiedToken,
        }
    });
    console.log(request.url);
    RestApiClient.performRequest(request,callback);
}

function simulateDevice(device,callback){
    let request = new Request(HOST.backend_api_simulations+ endpoint.simulation +'/'+ device.id,
        {method: 'GET'});
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function postDevice(user, callback){
    console.log(user);
    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    console.log('Bearer ' + modifiedToken);

    let request = new Request(HOST.backend_api + endpoint.device , {
        method: 'POST',
        headers : {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + modifiedToken,
        },
        body: JSON.stringify(user)
    });

    console.log("URL: " + request);

    RestApiClient.performRequest(request, callback);
}

function deleteDevice(device,callback){

    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    let request = new Request(HOST.backend_api+ endpoint.device +'/'+ device.id,
        {method: 'DELETE',
            headers : {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + modifiedToken,
            }});
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function putDevice(device,callback){

    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    let request = new Request(HOST.backend_api+ endpoint.device +'/'+ device.id,
        {method: 'PUT',
            headers : {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + modifiedToken,
            },
            body: JSON.stringify(device)
        });
    console.log(request.url);
    RestApiClient.performRequest(request, callback);
}

function assign(assignation, callback){

    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    let request = new Request(HOST.backend_api + endpoint.assignation, {
        method: 'POST',
        headers:{
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            Authorization: 'Bearer ' + modifiedToken,
        },
        body: JSON.stringify(assignation)
    });
    console.log(request.url);
    RestApiClient.performRequest(request,callback);
}

function unassign(assignation,callback){

    let token = localStorage.getItem('jwt');
    let modifiedToken = token.substring(1,token.length-1);

    let request = new Request(HOST.backend_api + endpoint.assignation +'/' + assignation.deviceId,{
        method: 'DELETE',
        headers:{
            Authorization: 'Bearer ' + modifiedToken,
        }});
    console.log(request.url);
    RestApiClient.performRequest(request,callback);
}
export {getDevices,postDevice,deleteDevice,putDevice,assign,unassign,simulateDevice};