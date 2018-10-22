const CosmosClient = require('@azure/cosmos').CosmosClient;
const express = require('express')
const morgan = require('morgan')
const url = require('url');
const bodyParser = require('body-parser');

const config = require('./config');

const endpoint = config.endpoint;
const masterKey = config.primaryKey;

const client = new CosmosClient({ endpoint: endpoint, auth: { masterKey: masterKey } });

const HttpStatusCodes = { NOTFOUND: 404 };

const databaseId = config.database.id;
const containerId = config.container.id;
console.log(databaseId)
async function readDatabase() {
    const {body: body, ref: datebase} = await client.database(databaseId).read();
    Object.keys(body).forEach( data => console.log(data) )
    Object.keys(datebase).forEach( data => console.log(data) )
    console.log(`Reading database:\n${datebase}\n`);
}

async function queryContainer() {
    console.log(`Querying container:\n${config.container.id}`);

    // query to return all children in a family
    const querySpec = {
        query: "SELECT r.lat, r.long , r.deviceID, r.riskFactor FROM root r WHERE r.deviceID = @deviceID",
        parameters: [
            {
                name: "@deviceID",
                value: "sensor_pi_1"
            }
        ]
    };

    const { result: results } = await client.database(databaseId).container(containerId).items.query(querySpec).toArray();
    for (var queryResult of results) {
        let resultString = JSON.stringify(queryResult);
        console.log(`\tQuery returned ${resultString}\n`);
    }
};


function exit(message) {
    console.log(message);
    console.log('Press any key to exit');
    process.stdin.setRawMode(true);
    process.stdin.resume();
    process.stdin.on('data', process.exit.bind(process, 0));
};

let app = express()
app.use(morgan('combined'))
app.use(express.static(path.join(__dirname, 'public'),
options))
app.use(bodyParser.urlencoded({
	extended: true
}))

let server = app.listen(process.env.PORT || 3000, function () {
	let host = server.address().address
	let port = server.address().port
	console.log('Example app listening at http://%s:%s', host, port)

})

queryContainer().then(() => { exit(`Completed successfully`); })
.catch((error) => { exit(`Completed with error ${JSON.stringify(error)}`) });
