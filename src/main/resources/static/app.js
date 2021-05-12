var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $(".table-conversation").show();
    }
    else {
        $(".table-conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    return new Promise(function(resolve, reject) {
        var socket = new SockJS('/gs-guide-websocket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/greetings', function (greeting) {
                var players = JSON.parse(greeting.body);
                $("#greetings").empty();
                players.forEach(function (player) {
                    showGreeting(player.content)
                })
            });
            stompClient.subscribe('/topic/guess', function (score) {
                var parsedscore = JSON.parse(score.body);
                if (parsedscore.number) {
                    showMatch(parsedscore.username + ' won! he found the number ' + parsedscore.number)
                    showMatch("a new game has started, guess again!")
                }
                else {
                    var match = parsedscore.correspondence;
                    if (match === '0')
                        match = 'none';
                    showMatch('<b>' + parsedscore.username + ':</b> ' + match);
                }
            });
            resolve();
        });
    })
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var name = $("#name");
    stompClient.send("/app/hello", {}, JSON.stringify({'name': name.val()}));
    name.val('')
}

function sendGuess() {
    var guess = $("#guess")
    stompClient.send("/app/guess", {}, JSON.stringify({'number': guess.val()}));
    guess.val('')
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
    $("#form-hello").hide();
    $("#form-guess").show();
}

function showMatch(message) {
    $("#guess-match").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { connect().then(sendName) });
    $( "#send-guess" ).click(function() { sendGuess(); });
    $('#guess').on("input drop", function() {
        if (!this.value.match(/^\d{0,4}$/) || this.value.split("").some(function(v,i,a){
            return (a.lastIndexOf(v)!=i)})) {
            console.log("replacing invalid/duplicate char");
            $("#guess").val(this.value.replace(/.$/, ''));
        }
    });
    $("#form-guess").hide();
});