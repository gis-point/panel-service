import React from 'react';
import {Table, Popconfirm} from 'antd';

const {
    RSocketClient,
    JsonSerializer,
    IdentitySerializer
} = require('rsocket-core');
const RSocketWebSocketClient = require('rsocket-websocket-client').default;
var port = '8080';
var ip = '37.187.164.210';
var url = 'http://' + ip + ':' + port;
var client = undefined;

export class CustomTable extends React.Component {
    state = {
        data: [
        ]
    };

    render() {
        return <Table columns={[
            {
                title: 'Номер',
                dataIndex: 'number',
                key: 'number',
            },
            {
                title: 'Табло',
                dataIndex: 'name',
                key: 'name'
            },
            {
                title: 'Час',
                dataIndex: 'time',
                key: 'time'
            },
            {
                title: 'Текст',
                dataIndex: 'text',
                key: 'text'
            },
            {
                title: '',
                render: (text, record) =>
                    this.state.data.length >= 1 ? (
                        <Popconfirm title="Перевантажити?" onConfirm={() => this.reloadPanel(record.number)}>
                            <a>Перевантажити</a>
                        </Popconfirm>
                    ) : null,
            },
            {
                title: '',
                render: (text, record) =>
                    this.state.data.length >= 1 ? (
                        <Popconfirm title="Змінити?" onConfirm={() => this.newServerSettings(record.number)}>
                            <a>Налаштування сервера</a>
                        </Popconfirm>
                    ) : null,
            }
        ]} dataSource={this.state.data}/>;
    }

    reloadPanel = (number) => {
        fetch(url + '/stop/' + number, {method: 'post'})
            .then(res => res.json())
            .then((data) => {
                console.log("reloaded");
                console.log(data);
            })
            .catch(console.log)
    };

    newServerSettings = (number) => {
        fetch(url + '/settings/' + number + '/' + ip + '/' + port, {method: 'post'})
            .then(res => res.json())
            .then((data) => {
                console.log("reloaded");
                console.log(data);
            })
            .catch(console.log)
    };

    componentDidMount() {
        if (client !== undefined) {
            client.close();
            document.getElementById("messages").innerHTML = "";
        }

        // Create an instance of a client
        client = new RSocketClient({
            serializers: {
                data: JsonSerializer,
                metadata: IdentitySerializer
            },
            setup: {
                // ms btw sending keepalive to server
                keepAlive: 60000,
                // ms timeout if no keepalive response
                lifetime: 180000,
                // format of `data`
                dataMimeType: 'application/json',
                // format of `metadata`
                metadataMimeType: 'message/x.rsocket.routing.v0',
            },
            transport: new RSocketWebSocketClient({
                url: 'ws://' + ip + ':' + port + '/panelsocket'
            }),
            responder: (data) => {
                console.log('Responder');
                console.log(data);
            }
        });

        // Open the connection
        client.connect().subscribe({
            onComplete: socket => {
                // socket provides the rsocket interactions fire/forget, request/response,
                // request/stream, etc as well as methods to close the socket.
                socket.requestStream({
                    data: {'panelId': '4910'},
                    metadata: String.fromCharCode('panels.by.id'.length) + 'panels.by.id',
                }).subscribe({
                    onComplete: () => {
                        console.log('complete')
                    },
                    onError: error => {
                        console.log(error);
                    },
                    onNext: payload => {
                        console.log(payload.data);
                        this.setState(state => {
                            var recieved = payload.data;

                            var neededPanel = "undefined";
                            for (var i in state.data) {
                                if (state.data[i].number === recieved.logicalNumber) {
                                    neededPanel = state.data[i];
                                    break;
                                }
                            }

                            if (neededPanel === "undefined") {
                                var newPanel = {
                                    number: recieved.logicalNumber,
                                    name: recieved.stopName,
                                    time: recieved.time,
                                    text: recieved.runningLineText.split("|").join("\n")
                                };
                                state.data.push(newPanel);
                            } else {
                                neededPanel.text = recieved.runningLineText.split("|").join("\n");
                            }
                            var newState = {
                                data: state.data
                            };
                            return newState;
                        });
                    },
                    onSubscribe: subscription => {
                        subscription.request(2147483647);
                        console.log('subscription: 2147483647');
                    },
                });
            },
            onError: error => {
                console.log(error);
            },
            onSubscribe: onSubData => {
                console.log('onSubData: ' + onSubData);
            }
        });
    }
}