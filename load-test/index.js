const axios = require('axios');
const uuidv4 = require('uuid/v4');
const baseUrl = 'http://localhost:80'; // with haproxy
// const baseUrl = 'http://localhost:8080'; // without haproxy
async function delivery(data){
    // console.log(`delivery - ${data.deliveryId}`);
    return axios.post(`${baseUrl}/ads/delivery`, data);
}

async function click(data){
    // console.log(`click - ${data.clickId}`);
    return axios.post(`${baseUrl}/ads/click`, data);
}

async function install(data){
    // console.log(`install - ${data.installId}`);
    return axios.post(`${baseUrl}/ads/install`, data);
}

function getDeliveryPayload(advertisementId, deliveryId) {
    return {
        advertisementId,
        deliveryId,
        'time': (new Date()).toISOString(),
        'browser': 'Chrome',
        'os': 'iOS',
        'site': 'http://super-dooper-news.com'
    };
}

function getClickPayload(deliveryId, clickId, ) {
    return {
        deliveryId,
        clickId,
        'time': (new Date()).toISOString(),
    };
}

function getInstallPayload(clickId, installId) {
    return {
        clickId,
        installId,
        'time': (new Date()).toISOString(),
    };
}

async function waitForResponse(promises) {
    try {
        await Promise.all(promises);
    } catch (error) {
        console.error(error);
    }
}
function wait(ms){
    var start = new Date().getTime();
    var end = start;
    while(end < start + ms) {
        end = new Date().getTime();
    }
}
async function hitAdServer(numberOfRequests = 100){
    const adId = Math.random() * 1000;
    const deliveryList = [];
    const clickList = [];
    const installList = [];
    for (let i = 0; i < numberOfRequests; i += 1) {
        deliveryList.push(uuidv4());
        clickList.push(uuidv4());
        installList.push(uuidv4());
    }
    let promises= [];
    console.log(new Date());
    console.time('delivery');
    for (let i = 0; i < numberOfRequests; i += 1) {
        // console.log(i+1);
        promises.push(delivery(getDeliveryPayload(adId, deliveryList[i])));
    }
    await waitForResponse(promises);
    console.timeEnd('delivery');
    promises=[];
    wait(1000);
    console.log(new Date());
    console.time('click');
    for (let i = 0; i < numberOfRequests; i += 1) {
        // console.log(i+1);
        promises.push(click(getClickPayload(deliveryList[i], clickList[i])));
    }
    await waitForResponse(promises);
    console.timeEnd('click');
    promises=[];
    wait(1000);
    console.log(new Date());
    console.time('install');
    for (let i = 0; i < numberOfRequests; i += 1) {
        // console.log(i+1);
        promises.push(install(getInstallPayload(clickList[i], installList[i])));
    }
    await waitForResponse(promises);
    console.timeEnd('install');
    promises=[];
    return "done";
}

console.time('hitting Adserver');
hitAdServer(100).then(function (res) {
    console.log(res);
    console.timeEnd('hitting Adserver');
})
    .catch(function (err) {
        console.error(err);
        console.timeEnd('hitting Adserver');
    });;
