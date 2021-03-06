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

app.get("/projecteur", function (req, res) {
    res.sendFile(path.join(__dirname + "/public/pages/projecteur.html"));
});

app.get("/projecteur_scores", function (req, res) {
    res.sendFile(path.join(__dirname + "/public/pages/projecteur_scores.html"));
});

// the players who joined the game {name: Alice, music_style: pop, score:2}
var players = [];

io.sockets.on("connect", function (socket, pseudo, music_style) {

    socket.on("join", function (data) {

        console.log("New player joined: " + data.pseudo + " with music style: " + data.music_style);
        socket.pseudo = data.pseudo;
        socket.music_style = data.music_style;
        players.push({ pseudo : data.pseudo, music_style : data.music_style, score : 0 });
        socket.broadcast.emit("join", players);
    });

    socket.on("gameOn", function () {

        console.log("Game started");
        socket.emit("init", players);
    });

    socket.on("buzz", function () {
        console.log("Player " + socket.pseudo + " buzzed!");
        socket.broadcast.emit("buzz", socket.pseudo);
    });

    socket.on("score", function (data) {

        console.log("Player " + data + " scored!");

        players = players.map(function (player) {
            if (player.pseudo == data) player.score++;
            return player;
        });

        socket.emit("update", players);
    });

    socket.on("displayResponse", function (data) {
        socket.broadcast.emit("displayResponse", data);
    });

    socket.on("displayScore", function () {
        socket.broadcast.emit("displayScore", players);
    });

    socket.on("displayResponse", function (data) {
        socket.broadcast.emit("displayResponse", data);
    });

    socket.on("disconnect", function() {

        players = players.filter(function (player) {
            return player.pseudo != socket.pseudo;
        });
        socket.broadcast.emit("update", players);
    });
});

server.listen(8080);
console.log("Listening on port 8080...");
