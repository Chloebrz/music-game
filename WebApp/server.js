/**
 * Server of the Music Game application.
 * Using express and socket.io.
 */

var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var path = require("path");

app.use(express.static(__dirname + "/public"));

app.get("/", function (req, res) {
    res.sendFile(path.join(__dirname + "/public/pages/index.html"));
});

app.get("/master", function (req, res) {
    res.sendFile(path.join(__dirname + "/public/pages/master.html"));
});

app.get("/player", function (req, res) {
    res.sendFile(path.join(__dirname + "/public/pages/player.html"));
});

// the usernames of the players who joined the game
// TODO: add the music styles
var players = [];

io.sockets.on("connect", function (socket, pseudo) {

    socket.on("join", function (pseudo) {

        console.log("New player joined: " + pseudo);
        socket.pseudo = pseudo;
        players.push(pseudo);
        socket.broadcast.emit("join", players);
    });

    socket.on("buzz", function () {
        console.log("Player " + socket.pseudo + " buzzed!");
        socket.broadcast.emit("buzz", socket.pseudo);
    });

    socket.on("disconnect", function() {
        players.splice(players.indexOf(socket.pseudo), 1);
        socket.broadcast.emit("left", players);
    });
});

server.listen(8080);
console.log("Listening on port 8080...");
