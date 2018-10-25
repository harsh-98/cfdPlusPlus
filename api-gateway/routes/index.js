const express = require('express')
const CosmosClient = require('@azure/cosmos').CosmosClient;
const url = require('url');
const fs = require('fs');

let router = express.Router();


const config = require('./../config');

const endpoint = config.endpoint;
const masterKey = config.primaryKey;

const client = new CosmosClient({ endpoint: endpoint, auth: { masterKey: masterKey } });


const databaseId = config.database.id;
const containerId = config.container.id;

async function readDatabase() {
    const {body: body, ref: datebase} = await client.database(databaseId).read();
    Object.keys(body).forEach( data => console.log(data) )
    Object.keys(datebase).forEach( data => console.log(data) )
    console.log(`Reading database:\n${datebase}\n`);
}

function otherNodes(count){
    var coord = [{lat: 20, long:20},{lat: 20, long:20},{lat: 20, long:20},{lat: 20, long:20}]
    let dateTime = Date.now()
    for(i=0;i<count;i++){
        let riskFactor = (Math.random() * 5);
        coord[i].riskFactor = riskFactor
        coord[i].lastUpdated = dateTime
        coord[i].deviceID = "sensor_pi_" + (i+2)
    }
    return coord
}

router.get('/', (req, res)=>{
    const index = fs.readFileSync( __dirname + '/../index.html');
    res.send(index)
})

router.get('/api',async (req, res) => {
        console.log(`Querying container: ${config.container.id}`);

        // query to return all children in a family
        const querySpec = {
            query: "SELECT TOP 1 r.lat, r.long , r.deviceID, r.createdDate as lastUpdated, r.riskFactor FROM root r WHERE r.deviceID = @deviceID",
            parameters: [
                {
                    name: "@deviceID",
                    value: "sensor_pi_1"
                }
            ]
        };
        const { result: results } = await client.database(databaseId).container(containerId).items.query(querySpec).toArray();
        const nodes = otherNodes(4)
        // nodes = nodes.concat(results)
        var node = {}
        for(var result of results){
            node = {
                lat: result.lat,
                long: result.long,
                deviceID: result.deviceID,
                lastUpdated: result.lastUpdated,
                riskFactor: result.riskFactor
            }
        }
        nodes.push(node)
        res.status(200).json(nodes);
})



module.exports = router
